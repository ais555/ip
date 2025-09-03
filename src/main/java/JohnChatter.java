import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;
import java.util.ArrayList;

public class JohnChatter {
    private Storage storage;

    public JohnChatter(String filepath) {
        this.storage = new Storage(filepath);
    }

    public void run() throws IOException {
        System.out.println("it is i, john chatter");
        ArrayList<Task> items = this.storage.load();
        String input;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            // Wait for user inputs. Breaks out and returns when the command "bye" is given
            input = scanner.nextLine();
            String[] splitInputAroundSpace = input.split(" ");

            try {
                if (input.equals("bye")) {
                    System.out.println("goodbye!");
                    return;
                } else if (input.equals("list")) {
                    for (int i = 1; i <= items.size(); i++) {
                        Task item = items.get(i -1);
                        if (item != null) {
                            System.out.println(i + "." + item);
                        }
                    }
                } else if (splitInputAroundSpace.length == 2 && splitInputAroundSpace[0].equals("mark") && splitInputAroundSpace[1].matches("\\d+")) {
                    // Mark a task as done
                    int index = Integer.parseInt(splitInputAroundSpace[1]);
                    if (items.get(index - 1) != null) {
                        items.get(index - 1).markAsDone();
                        System.out.println("marked " + items.get(index - 1).description + " as done");
                    } else {
                        System.out.println("i cannot mark a task that doesn't exist yet");
                    }
                } else if (splitInputAroundSpace.length == 2 && splitInputAroundSpace[0].equals("unmark") && splitInputAroundSpace[1].matches("\\d+")) {
                    // Mark a task as undone
                    int index = Integer.parseInt(splitInputAroundSpace[1]);
                    if (items.get(index - 1) != null) {
                        items.get(index - 1).markAsUndone();
                        System.out.println("marked " + items.get(index - 1).description + " as undone");
                    } else {
                        System.out.println("i cannot mark a task that doesn't exist yet");
                    }
                } else if (splitInputAroundSpace[0].equals("todo")) {
                    if (splitInputAroundSpace.length == 1) {
                        throw new JohnChatterException("oops! tasks must have a description");
                    }
                    Todo todo = new Todo(input.split("todo ")[1]);
                    items.add(todo);
                    try {
                        this.storage.writeTaskData(items);
                    } catch (IOException e) {
                        System.out.println("something went wrong: " + e.getMessage());
                    }
                    System.out.println("added:\n" + todo);
                } else if (splitInputAroundSpace[0].equals("deadline")) {
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
                    items.add(deadline);

                    try {
                        this.storage.writeTaskData(items);
                    } catch (IOException e) {
                        System.out.println("something went wrong: " + e.getMessage());
                    }
                    System.out.println("added:\n" + deadline);
                } else if (splitInputAroundSpace[0].equals("event")) {
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
                    items.add(event);
                    try {
                        this.storage.writeTaskData(items);
                    } catch (IOException e) {
                        System.out.println("something went wrong: " + e.getMessage());
                    }
                    System.out.println("added:\n" + event);
                } else if (splitInputAroundSpace[0].equals("delete")) {
                    if (splitInputAroundSpace.length == 2 && splitInputAroundSpace[1].matches("\\d+")) {
                        int number = Integer.parseInt(splitInputAroundSpace[1]);
                        String task = items.get(number - 1).toString();
                        items.remove(number - 1);
                        System.out.println("deleted task: " + task);
                        try {
                            this.storage.writeTaskData(items);
                        } catch (IOException e) {
                            System.out.println("something went wrong: " + e.getMessage());
                        }
                    }
                } else {
                    throw new JohnChatterException("sorry, i don't know what that means");
                }
            } catch (JohnChatterException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) throws JohnChatterException, IOException {
        new JohnChatter("data/task_data.txt").run();
    }
}
