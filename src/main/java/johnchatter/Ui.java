package johnchatter;

/**
 * Handles interactions with the user.
 */
public class Ui {
    public Ui() {
    }

    public void showWelcome() {
        System.out.println("it is i, john chatter");
    }

    public void showGoodbye() {
        System.out.println("goodbye!");
    }

    public void showDividerLine() {
        System.out.println("------------------");
    }

    public void showError(String message) {
        System.out.println("something went wrong: " + message);
    }
}
