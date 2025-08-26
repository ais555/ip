import java.util.Scanner;

public class JohnChatter {
    public static void main(String[] args) {
        System.out.println("it is i, john chatter");

        String[] items = new String[100];
        int nextItem = 0;

        String input = "";
        Scanner scanner = new Scanner(System.in);
        while (true) {
            input = scanner.nextLine();
            if (input.equals("bye")) {
                System.out.println("goodbye!");
                return;
            } else if (input.equals("list")) {
                for (int i = 1; i <= items.length; i++) {
                    if (items[i -1] != null) {
                        System.out.println(i + ". " + items[i - 1]);
                    }
                }
            } else {
                System.out.println("added: " + input);
                items[nextItem] = input;
                nextItem++;
            }
        }
    }
}
