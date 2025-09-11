package johnchatter;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Contains the methods for operating on the list of tasks.
 */
public class TaskList {
    public ArrayList<Task> list;

    public TaskList(ArrayList<Task> list) {
        this.list = list;
    }

    public String mark(Task task) {
        if (task != null) {
            task.markAsDone();
            return "marked " + task.description + " as done";
        } else {
            return "i cannot mark a task that doesn't exist yet";
        }
    }

    public String unmark(Task task) {
        if (task != null) {
            task.markAsUndone();
            return "marked " + task.description + " as undone";
        } else {
            return "i cannot mark a task that doesn't exist yet";
        }
    }

    public String addTodo(Todo todo, Storage storage, Ui ui) {
        list.add(todo);
        try {
            storage.writeTaskData(list);
        } catch (IOException e) {
            ui.showError(e.getMessage());
        }
        return "added:\n" + todo;
    }

    public String addDeadline(Deadline deadline, Storage storage, Ui ui) {
        list.add(deadline);
        try {
            storage.writeTaskData(list);
        } catch (IOException e) {
            ui.showError(e.getMessage());
        }
        return "added:\n" + deadline;
    }

    public String addEvent(Event event, Storage storage, Ui ui) {
        list.add(event);
        try {
            storage.writeTaskData(list);
        } catch (IOException e) {
            ui.showError(e.getMessage());
        }
        return "added:\n" + event;
    }

    public String deleteTask(Task task, Storage storage, Ui ui) {
        list.remove(task);
        try {
            storage.writeTaskData(list);
        } catch (IOException e) {
            ui.showError(e.getMessage());
        }
        return "deleted task: " + task;
    }
}
