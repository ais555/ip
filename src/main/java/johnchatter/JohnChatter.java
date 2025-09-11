package johnchatter;

import java.io.IOException;
import java.util.Scanner;

public class JohnChatter {
    private final Storage storage;
    private final TaskList tasks;
    private final Ui ui;

    public JohnChatter(String filepath) throws IOException {
        this.storage = new Storage(filepath);
        this.tasks = new TaskList(this.storage.load());
        this.ui = new Ui();
    }

    public String getResponse(String input) {
        try {
            return Parser.parse(input, ui, storage, tasks);
        } catch (JohnChatterException e) {
            return e.getMessage();
        }
    }

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
            } catch (JohnChatterException e) {
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
