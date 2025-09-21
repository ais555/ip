package johnchatter;

import java.io.IOException;
import java.util.Scanner;

public class JohnChatter {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    /**
     * Constructs the JohnChatter object and initialises storage, tasks, and ui.
     *
     * @param filepath The filepath of the text file that stores task data
     */
    public JohnChatter(String filepath) throws IOException {
        assert filepath != null : "Filepath should not be null";

        this.storage = new Storage(filepath);
        assert this.storage != null : "Storage should not be null";

        this.tasks = new TaskList(this.storage.load());
        assert this.tasks != null : "Task list should not be null";

        this.ui = new Ui();
        assert this.ui != null : "Ui should not be null";
    }

    /**
     * Gets John Chatter's response to a given user input.
     *
     * @return The response that will be displayed in the DialogBox
     */
    public String getResponse(String input) {
        assert input != null : "Input should not be null";
        try {
            String response = Parser.parse(input, ui, storage, tasks);
            assert response != null : "response should not be null";
            return response;
        } catch (JohnChatterException | IOException e) {
            return e.getMessage();
        }
    }

    /**
     * Handles the app's main functionality loop of scanning user input then responding appropriately.
     * Terminates when the "bye" command is given.
     */
    public void run() {
        ui.showWelcome();
        String input;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            input = scanner.nextLine();
            try {
                ui.showDividerLine();
                if (Parser.parse(input, ui, storage, tasks).equals("bye")) {
                    break;
                }
            } catch (JohnChatterException | IOException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showDividerLine();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        new JohnChatter("data/task_data.txt").run();
    }
}
