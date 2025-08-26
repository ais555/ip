import java.util.Scanner;

public class JohnChatter {
    public static void main(String[] args) {
        System.out.println("it is i, john chatter");

        String input = "";
        Scanner scanner = new Scanner(System.in);
        while (true) {
            input = scanner.nextLine();
            if (input.equals("bye")) {
                System.out.println("goodbye!");
                return;
            } else {
                System.out.println(input);
            }
        }
    }
}
