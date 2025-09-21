package johnchatter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Handles the parsing of commands from user input.
 */
public class Parser {
    private static final String ERROR_TODO_NO_DESCRIPTION =
            "oops! todo must have a description";
    private static final String ERROR_DEADLINE_NO_BY =
            "oops! deadline must have a /by date";
    private static final String ERROR_EVENT_NO_FROM_TO =
            "oops! event must have /from and /to dates";
    private static final String ERROR_INVALID_TASK_NUMBER =
            "it seems you have input an invalid task number, please check and try again";
    private static final String ERROR_UNKNOWN_COMMAND =
            "sorry, i don't know what that means";

    // ChatGPT was used to abstract out the helper functions from the parse method

    /**
     * Parses the user input and performs the appropriate action for the command given.
     *
     * @return John Chatter's text response that will be displayed to the user
     * @throws JohnChatterException If the user issues an invalid command
     */
    public static String parse(String input, Ui ui, Storage storage, TaskList tasks) throws JohnChatterException {
        assert input != null : "Input should not be null";
        assert ui != null : "Ui should not be null";
        assert storage != null : "Storage should not be null";
        assert tasks != null : "Task list should not be null";

        ArrayList<Task> list = tasks.list;
        String[] tokens = input.split(" ");
        String command = tokens[0];

        switch (command) {
        case "bye":
            return handleBye(ui);
        case "list":
            return handleList(list);
        case "find":
            return handleFind(tokens, list);
        case "findtag":
            return handleFindTag(tokens, list);
        case "mark":
            return handleMark(tokens, list, tasks);
        case "unmark":
            return handleUnmark(tokens, list, tasks);
        case "todo":
            return handleTodo(input, storage, tasks, ui);
        case "deadline":
            return handleDeadline(input, storage, tasks, ui);
        case "event":
            return handleEvent(input, storage, tasks, ui);
        case "delete":
            return handleDelete(tokens, list, tasks, storage, ui);
        case "tag":
            return handleTag(tokens, list, tasks);
        case "untag":
            return handleUntag(tokens, list, tasks);
        default:
            throw new JohnChatterException(ERROR_UNKNOWN_COMMAND);
        }
    }

    private static String handleBye(Ui ui) {
        ui.showGoodbye();
        return "bye";
    }

    private static String handleList(ArrayList<Task> list) {
        return formatTaskList(list);
    }

    // ChatGPT was used to improve the handleFind and handleFindTag methods
    private static String handleFind(String[] tokens, ArrayList<Task> list) throws JohnChatterException {
        if (tokens.length < 2) {
            throw new JohnChatterException("usage: find <keyword1> <keyword2> ...");
        }

        // all search keywords, lowercase for case-insensitive matching
        String[] keywords = new String[tokens.length - 1];
        System.arraycopy(tokens, 1, keywords, 0, tokens.length - 1);

        ArrayList<Task> filteredList = new ArrayList<>(list.stream()
                .filter(task -> {
                    String desc = task.description.toLowerCase();
                    // must match ALL keywords
                    for (String keyword : keywords) {
                        if (!desc.contains(keyword.toLowerCase())) {
                            return false;
                        }
                    }
                    return true;
                })
                .toList());

        return formatTaskList(filteredList);
    }

    private static String handleFindTag(String[] tokens, ArrayList<Task> list) throws JohnChatterException {
        if (tokens.length < 2) {
            throw new JohnChatterException("usage: findtag <tag1> <tag2> ...");
        }

        String[] tags = new String[tokens.length - 1];
        System.arraycopy(tokens, 1, tags, 0, tokens.length - 1);

        ArrayList<Task> filteredList = new ArrayList<>(list.stream()
                .filter(task -> {
                    String desc = task.description.toLowerCase();
                    // must match all tags
                    for (String tag : tags) {
                        if (!task.getTags().contains(tag.toLowerCase())) {
                            return false;
                        }
                    }
                    return true;
                })
                .toList());

        return formatTaskList(filteredList);
    }

    private static String formatTaskList(ArrayList<Task> list) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            Task item = list.get(i);
            if (item == null) {
                continue;
            }
            result.append(i + 1).append(".").append(item).append("\n");
        }
        return result.toString();
    }

    private static String handleMark(String[] tokens, ArrayList<Task> list, TaskList tasks) throws JohnChatterException {
        int index = parseIndex(tokens, list);
        return tasks.mark(list.get(index));
    }

    private static String handleUnmark(String[] tokens, ArrayList<Task> list, TaskList tasks) throws JohnChatterException {
        int index = parseIndex(tokens, list);
        return tasks.unmark(list.get(index));
    }

    private static String handleTodo(String input, Storage storage, TaskList tasks, Ui ui) throws JohnChatterException {
        if (!input.contains("todo ")) {
            throw new JohnChatterException(ERROR_TODO_NO_DESCRIPTION);
        }
        Todo todo = new Todo(input.split("todo ")[1]);

        return tasks.addTodo(todo, storage, ui);
    }

    private static String handleDeadline(String input, Storage storage, TaskList tasks, Ui ui) throws JohnChatterException {
        if (!input.contains("/by ")) {
            throw new JohnChatterException(ERROR_DEADLINE_NO_BY);
        }
        String description = input.split("deadline ")[1].split(" /by")[0];
        String deadlineDateInput = input.split("/by ")[1];
        String formattedDate = formatDate(deadlineDateInput);

        Deadline deadline = new Deadline(description, formattedDate);
        return tasks.addDeadline(deadline, storage, ui);
    }

    private static String handleEvent(String input, Storage storage, TaskList tasks, Ui ui) throws JohnChatterException {
        if (!input.contains("/from ") || !input.contains("/to ")) {
            throw new JohnChatterException(ERROR_EVENT_NO_FROM_TO);
        }
        String description = input.split(" /from")[0].split("event ")[1];
        String startInput = input.split(" /to")[0].split("/from ")[1];
        String endInput = input.split("/to ")[1];

        String formattedStart = formatDate(startInput);
        String formattedEnd = formatDate(endInput);

        Event event = new Event(description, formattedStart, formattedEnd);
        return tasks.addEvent(event, storage, ui);
    }

    private static String handleDelete(String[] tokens, ArrayList<Task> list, TaskList tasks, Storage storage, Ui ui) throws JohnChatterException {
        int index = parseIndex(tokens, list);
        Task task = list.get(index);
        return tasks.deleteTask(task, storage, ui);
    }

    private static int parseIndex(String[] tokens, ArrayList<Task> list) throws JohnChatterException {
        if (tokens.length != 2 || !tokens[1].matches("\\d+")) {
            throw new JohnChatterException(ERROR_INVALID_TASK_NUMBER);
        }
        int index = Integer.parseInt(tokens[1]) - 1;
        if (index < 0 || index >= list.size()) {
            throw new JohnChatterException(ERROR_INVALID_TASK_NUMBER);
        }
        return index;
    }

    private static String formatDate(String input) {
        try {
            LocalDate date = LocalDate.parse(input);
            return date.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
        } catch (DateTimeParseException e) {
            return input;
        }
    }

    // ChatGPT was used to write this method
    private static String handleTag(String[] tokens, ArrayList<Task> list, TaskList tasks) throws JohnChatterException {
        if (tokens.length != 3) {
            throw new JohnChatterException("usage: tag <task_number> <#tag>");
        }
        int index = parseIndex(new String[]{tokens[0], tokens[1]}, list);
        String tag = tokens[2];
        Task task = list.get(index);
        task.addTag(tag);
        return "added tag " + tag + " to task: " + task;
    }

    // ChatGPT was used to write this method
    private static String handleUntag(String[] tokens, ArrayList<Task> list, TaskList tasks) throws JohnChatterException {
        if (tokens.length != 3) {
            throw new JohnChatterException("usage: untag <task_number> <#tag>");
        }
        int index = parseIndex(new String[]{tokens[0], tokens[1]}, list);
        String tag = tokens[2];
        Task task = list.get(index);
        if (task.getTags().contains(tag)) {
            task.removeTag(tag);
            return "removed tag " + tag + " from task: " + task;
        } else {
            return "task does not have tag " + tag;
        }
    }
}
