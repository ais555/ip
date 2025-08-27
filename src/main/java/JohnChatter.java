import java.util.Scanner;

public class JohnChatter {
    public static void main(String[] args) {
        System.out.println("it is i, john chatter");

        Task[] items = new Task[100];
        int nextItem = 0;

        String input = "";
        Scanner scanner = new Scanner(System.in);
        while (true) {
            // Wait for user inputs. Breaks out and returns when the command "bye" is given
            input = scanner.nextLine();
            String[] splitInputAroundSpace = input.split(" ");

            if (input.equals("bye")) {
                System.out.println("goodbye!");
                return;
            } else if (input.equals("list")) {
                for (int i = 1; i <= items.length; i++) {
                    Task item = items[i - 1];
                    if (item != null) {
                        System.out.println(i + "." + item.toString());
                    }
                }
            } else if (splitInputAroundSpace.length == 2 && splitInputAroundSpace[0].equals("mark") && splitInputAroundSpace[1].matches("\\d+")) {
                // Mark a task as done
                int index = Integer.parseInt(splitInputAroundSpace[1]);
                if (items[index - 1] != null) {
                    items[index - 1].markAsDone();
                    System.out.println("marked " + items[index - 1].description + " as done");
                } else {
                    System.out.println("i cannot mark a task that doesn't exist yet");
                }
            } else if (splitInputAroundSpace.length == 2 && splitInputAroundSpace[0].equals("unmark") && splitInputAroundSpace[1].matches("\\d+")) {
                // Mark a task as undone
                int index = Integer.parseInt(splitInputAroundSpace[1]);
                if (items[index - 1] != null) {
                    items[index - 1].markAsUndone();
                    System.out.println("marked " + items[index - 1].description + " as undone");
                } else {
                    System.out.println("i cannot mark a task that doesn't exist yet");
                }
            } else if (splitInputAroundSpace[0].equals("todo")) {
                Todo todo = new Todo(input.split("todo ")[1]);
                items[nextItem] = todo;
                nextItem++;
                System.out.println("added:\n" + todo.toString());
            } else if (splitInputAroundSpace[0].equals("deadline")) {
                Deadline deadline = new Deadline(input.split("deadline ")[1].split(" /by")[0], input.split("/by ")[1]);
                items[nextItem] = deadline;
                nextItem++;
                System.out.println("added:\n" + deadline.toString());
            } else if (splitInputAroundSpace[0].equals("event")) {
                String end = input.split("/to ")[1];
                String start = input.split(" /to")[0].split("/from ")[1];
                String description = input.split(" /to")[0].split(" /from")[0].split("event ")[1];
                Event event = new Event(description, start, end);
                items[nextItem] = event;
                nextItem++;
                System.out.println("added:\n" + event.toString());
            }
        }
    }
}
