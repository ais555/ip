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
        ArrayList<Task> list = tasks.list;
        String[] splitInputAroundSpace = input.split(" ");
        if (input.equals("bye")) {
            ui.showGoodbye();
            return "bye";
        } else if (input.equals("list")) {
            for (int i = 1; i <= list.size(); i++) {
                Task item = list.get(i - 1);
                if (item != null) {
                    System.out.println(i + "." + item);
                }
            }
        } else if (splitInputAroundSpace.length == 2 && splitInputAroundSpace[0].equals("find")) {
            String keyword = splitInputAroundSpace[1];
            ArrayList<Task> filteredList = new ArrayList<>(list.stream()
                    .filter(task -> task.description.contains(keyword))
                    .collect(Collectors.toList()));
            for (int i = 1; i <= filteredList.size(); i++) {
                Task item = filteredList.get(i - 1);
                if (item != null) {
                    System.out.println(i + "." + item);
                }
            }
        } else if (splitInputAroundSpace.length == 2 && splitInputAroundSpace[0].equals("mark") && splitInputAroundSpace[1].matches("\\d+")) {
            // Mark a task as done
            int index = Integer.parseInt(splitInputAroundSpace[1]);
            if (index > list.size()) {
                throw new JohnChatterException(
                        "it seems you have input an invalid task number, please check and try again");
            }
            tasks.mark(list.get(index - 1));
        } else if (splitInputAroundSpace.length == 2 && splitInputAroundSpace[0].equals("unmark") && splitInputAroundSpace[1].matches("\\d+")) {
            // Mark a task as undone
            int index = Integer.parseInt(splitInputAroundSpace[1]);
            if (index > list.size()) {
                throw new JohnChatterException(
                        "it seems you have input an invalid task number, please check and try again");
            }
            tasks.unmark(list.get(index - 1));
        } else if (splitInputAroundSpace[0].equals("todo")) {
            // Add a Todo
            if (splitInputAroundSpace.length == 1) {
                throw new JohnChatterException("oops! tasks must have a description");
            }

            Todo todo = new Todo(input.split("todo ")[1]);
            tasks.addTodo(todo, storage, ui);
        } else if (splitInputAroundSpace[0].equals("deadline")) {
            // Add a Deadline
            if (splitInputAroundSpace.length == 1) {
                throw new JohnChatterException("oops! tasks must have a description");
            }
            String formattedDeadlineDate;
            String deadlineDateInput = input.split("/by ")[1];
            try {
                LocalDate deadlineDate = LocalDate.parse(deadlineDateInput);
                formattedDeadlineDate = deadlineDate.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
            } catch (DateTimeParseException e) {
                formattedDeadlineDate = deadlineDateInput;
            }

            Deadline deadline = new Deadline(
                    input.split("deadline ")[1].split(" /by")[0], formattedDeadlineDate);
            tasks.addDeadline(deadline, storage, ui);
        } else if (splitInputAroundSpace[0].equals("event")) {
            // Add an Event
            if (splitInputAroundSpace.length == 1) {
                throw new JohnChatterException("oops! tasks must have a description");
            }
            String formattedEnd;
            String formattedStart;
            String endInput = input.split("/to ")[1];
            String startInput = input.split(" /to")[0].split("/from ")[1];
            String description = input.split(" /to")[0].split(" /from")[0].split("event ")[1];
            try {
                LocalDate startDate = LocalDate.parse(startInput);
                formattedStart = startDate.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
            } catch (DateTimeParseException e) {
                formattedStart = startInput;
            }
            try {
                LocalDate endDate = LocalDate.parse(endInput);
                formattedEnd = endDate.format(DateTimeFormatter.ofPattern("MMM d yyyy"));
            } catch (DateTimeParseException e) {
                formattedEnd = endInput;
            }

            Event event = new Event(description, formattedStart, formattedEnd);
            tasks.addEvent(event, storage, ui);
        } else if (splitInputAroundSpace[0].equals("delete")) {
            if (splitInputAroundSpace.length == 2 && splitInputAroundSpace[1].matches("\\d+")) {
                int index = Integer.parseInt(splitInputAroundSpace[1]);
                if (index > list.size()) {
                    throw new JohnChatterException(
                            "it seems you have input an invalid task number, please check and try again");
                }
                Task task = list.get(index - 1);
                tasks.deleteTask(task, storage, ui);
            }
        } else {
            throw new JohnChatterException("sorry, i don't know what that means");
        }
        return "";
    }
}
