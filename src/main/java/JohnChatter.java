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
            String[] splitInput = input.split(" ");

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
            } else if (splitInput.length == 2 && splitInput[0].equals("mark") && splitInput[1].matches("\\d+")) {
                // Mark a task as done
                int index = Integer.parseInt(splitInput[1]);
                if (items[index - 1] != null) {
                    items[index - 1].markAsDone();
                    System.out.println("marked " + items[index - 1].description + " as done");
                } else {
                    System.out.println("i cannot mark a task that doesn't exist yet");
                }
            } else if (splitInput.length == 2 && splitInput[0].equals("unmark") && splitInput[1].matches("\\d+")) {
                // Mark a task as undone
                int index = Integer.parseInt(splitInput[1]);
                if (items[index - 1] != null) {
                    items[index - 1].markAsUndone();
                    System.out.println("marked " + items[index - 1].description + " as undone");
                } else {
                    System.out.println("i cannot mark a task that doesn't exist yet");
                }
            } else {
                System.out.println("added: " + input);
                items[nextItem] = new Task(input);
                nextItem++;
            }
        }
    }
}
