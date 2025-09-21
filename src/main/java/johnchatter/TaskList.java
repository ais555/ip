package johnchatter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Contains the methods for operating on the list of tasks.
 */
public class TaskList {
    public ArrayList<Task> list;

    /**
     * Constructs the TaskList.
     */
    public TaskList(ArrayList<Task> list) {
        this.list = list;
    }

    /**
     * Marks a task as completed.
     */
    public String mark(Task task, Storage storage) throws IOException {
        if (task == null) {
            return "i cannot mark a task that doesn't exist yet";
        }

        task.markAsDone();
        storage.writeTaskData(list);
        return "marked " + task.description + " as done";
    }

    /**
     * Marks a task as incomplete.
     */
    public String unmark(Task task, Storage storage) throws IOException {
        if (task == null) {
            return "i cannot mark a task that doesn't exist yet";
        }

        task.markAsUndone();
        storage.writeTaskData(list);
        return "marked " + task.description + " as undone";
    }

    /**
     * Adds a Todo to the list and writes the new data to file.
     */
    public String addTodo(Todo todo, Storage storage, Ui ui) {
        assert todo != null : "todo should not be null";
        assert ui != null : "Ui should not be null";
        assert storage != null : "Storage should not be null";

        list.add(todo);
        try {
            storage.writeTaskData(list);
        } catch (IOException e) {
            ui.showError(e.getMessage());
        }
        return "added:\n" + todo;
    }

    /**
     * Adds a Deadline to the list and writes the new data to file.
     */
    public String addDeadline(Deadline deadline, Storage storage, Ui ui) {
        assert deadline != null : "deadline should not be null";
        assert ui != null : "Ui should not be null";
        assert storage != null : "Storage should not be null";

        list.add(deadline);
        try {
            storage.writeTaskData(list);
        } catch (IOException e) {
            ui.showError(e.getMessage());
        }
        return "added:\n" + deadline;
    }

    /**
     * Adds an Event to the list and writes the new data to file.
     */
    public String addEvent(Event event, Storage storage, Ui ui) {
        assert event != null : "event should not be null";
        assert ui != null : "Ui should not be null";
        assert storage != null : "Storage should not be null";

        list.add(event);
        try {
            storage.writeTaskData(list);
        } catch (IOException e) {
            ui.showError(e.getMessage());
        }
        return "added:\n" + event;
    }

    /**
     * Deletes a Task from the list and writes the updated data to file.
     */
    public String deleteTask(Task task, Storage storage, Ui ui) {
        assert task != null : "task should not be null";
        assert ui != null : "Ui should not be null";
        assert storage != null : "Storage should not be null";

        list.remove(task);
        try {
            storage.writeTaskData(list);
        } catch (IOException e) {
            ui.showError(e.getMessage());
        }
        return "deleted task: " + task;
    }
}
