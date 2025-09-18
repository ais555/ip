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
        default:
            throw new JohnChatterException("sorry, i don't know what that means");
        }
    }

    private static String handleBye(Ui ui) {
        ui.showGoodbye();
        return "bye";
    }

    private static String handleList(ArrayList<Task> list) {
        return formatTaskList(list);
    }

    private static String handleFind(String[] tokens, ArrayList<Task> list) {
        if (tokens.length != 2) {
            return "";
        }
        String keyword = tokens[1];
        ArrayList<Task> filteredList = new ArrayList<>(list.stream()
                .filter(task -> task.description.contains(keyword))
                .collect(Collectors.toList()));

        return formatTaskList(filteredList);
    }

    private static String formatTaskList(ArrayList<Task> list) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            Task item = list.get(i);
            if (item != null) {
                result.append(i + 1).append(".").append(item).append("\n");
            }
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
            throw new JohnChatterException("oops! tasks must have a description");
        }
        Todo todo = new Todo(input.split("todo ")[1]);

        return tasks.addTodo(todo, storage, ui);
    }

    private static String handleDeadline(String input, Storage storage, TaskList tasks, Ui ui) throws JohnChatterException {
        if (!input.contains("/by ")) {
            throw new JohnChatterException("oops! deadline must have a /by");
        }
        String description = input.split("deadline ")[1].split(" /by")[0];
        String deadlineDateInput = input.split("/by ")[1];
        String formattedDate = formatDate(deadlineDateInput);

        Deadline deadline = new Deadline(description, formattedDate);
        return tasks.addDeadline(deadline, storage, ui);
    }

    private static String handleEvent(String input, Storage storage, TaskList tasks, Ui ui) throws JohnChatterException {
        if (!input.contains("/from ") || !input.contains("/to ")) {
            throw new JohnChatterException("oops! event must have /from and /to");
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
            throw new JohnChatterException("please provide a valid task number");
        }
        int index = Integer.parseInt(tokens[1]) - 1;
        if (index < 0 || index >= list.size()) {
            throw new JohnChatterException("it seems you have input an invalid task number, please check and try again");
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
}
