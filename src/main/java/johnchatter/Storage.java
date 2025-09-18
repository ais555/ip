package johnchatter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Stores list data and handles writing and reading file operations to save and load data
 */
public class Storage {
    String filePath;
    File file;

    /**
     * Constructs the Storage object, initialising the file that is to be written to and read from.
     */
    public Storage(String filePath) {
        assert filePath != null : "filePath should not be null";
        this.filePath = filePath;
        this.file = new File(this.filePath);
    }

    /**
     * Writes task data to the file output, with attributes delineated by pipes.
     * Called after every operation that makes a change to the list of tasks.
     *
     * @param items The updated list of tasks
     * @throws IOException If an exception occurs in writing
     */
    public void writeTaskData(ArrayList<Task> items) throws IOException {
        assert items != null : "ArrayList should not be null";
        assert this.file != null : "file to write to should not be null";

        FileWriter writer = new FileWriter(this.file);
        for (Task item : items) {
            StringBuilder nextLine = new StringBuilder();
            if (item instanceof Todo) {
                nextLine.append("T|")
                        .append(item.isDone ? "1" : "0").append("|")
                        .append(item.description);
            } else if (item instanceof Deadline deadline) {
                nextLine.append("D|")
                        .append(deadline.isDone ? "1" : "0").append("|")
                        .append(deadline.description).append("|")
                        .append(deadline.by);
            } else if (item instanceof Event event) {
                nextLine.append("E|")
                        .append(event.isDone ? "1" : "0").append("|")
                        .append(event.description).append("|")
                        .append(event.start).append("|")
                        .append(event.end);
            }
            writer.write(nextLine.toString());
            writer.write(System.lineSeparator());
        }
        writer.close();
    }

    /**
     * Reads task data from the disk, which is then used by the rest of the program.
     *
     * @return ArrayList<Task> The saved list of tasks
     * @throws IOException If an exception occurs in scanning
     */
    public ArrayList<Task> loadTaskData() throws IOException {
        ArrayList<Task> items = new ArrayList<>();

        assert this.file != null : "file to load from should not be null";
        Scanner scanner = new Scanner(this.file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            String[] parts = line.split("\\|");
            String taskType = parts[0];
            boolean isDone = parts[1].equals("1");

            switch (taskType) {
            case "T":
                Todo todo = new Todo(parts[2]);
                if (isDone) todo.markAsDone();
                items.add(todo);
                break;
            case "D":
                Deadline d = new Deadline(parts[2], parts[3]);
                if (isDone) d.markAsDone();
                items.add(d);
                break;
            case "E":
                Event e = new Event(parts[2], parts[3], parts[4]);
                if (isDone) e.markAsDone();
                items.add(e);
                break;
            default:
                System.out.println("unrecognised task type");
            }
        }
        return items;
    }

    /**
     * Creates the directories where the data will be stored if necessary, before loading the data.
     *
     * @return ArrayList<Task>
     * @throws IOException If the necessary directories fail to be created
     */
    public ArrayList<Task> load() throws IOException {
        File taskDataParent = this.file.getParentFile();
        if (!taskDataParent.exists()) {
            if (!taskDataParent.mkdirs()) {
                throw new IOException("mkdirs failed");
            }
        }
        if (!this.file.exists()) {
            if (!this.file.createNewFile()) {
                throw new IOException("createNewFile failed");
            }
        }
        return loadTaskData();
    }
}
