import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;

public class JohnChatter {
    private Storage storage;
    private Ui ui;

    public JohnChatter(String filepath) {
        this.storage = new Storage(filepath);
        this.ui = new Ui();
    }

    public void run() throws IOException, JohnChatterException {
        ui.showWelcome();
        ArrayList<Task> items = this.storage.load();
        String input;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            input = scanner.nextLine();
            try {
                ui.showDividerLine();
                if (Parser.parse(input, ui, storage, items).equals("bye")) {
                    break;
                }
            } catch (JohnChatterException e) {
                ui.showError(e.getMessage());
            } finally {
                ui.showDividerLine();
            }
        }
    }

    public static void main(String[] args) throws JohnChatterException, IOException {
        new JohnChatter("data/task_data.txt").run();
    }
}
