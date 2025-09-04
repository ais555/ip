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

    public void mark(Task task) {
        if (task != null) {
            task.markAsDone();
            System.out.println("marked " + task.description + " as done");
        } else {
            System.out.println("i cannot mark a task that doesn't exist yet");
        }
    }

    public void unmark(Task task) {
        if (task != null) {
            task.markAsUndone();
            System.out.println("marked " + task.description + " as undone");
        } else {
            System.out.println("i cannot mark a task that doesn't exist yet");
        }
    }

    public void addTodo(Todo todo, Storage storage, Ui ui) {
        list.add(todo);
        try {
            storage.writeTaskData(list);
        } catch (IOException e) {
            ui.showError(e.getMessage());
        }
        System.out.println("added:\n" + todo);
    }

    public void addDeadline(Deadline deadline, Storage storage, Ui ui) {
        list.add(deadline);
        try {
            storage.writeTaskData(list);
        } catch (IOException e) {
            ui.showError(e.getMessage());
        }
        System.out.println("added:\n" + deadline);
    }

    public void addEvent(Event event, Storage storage, Ui ui) {
        list.add(event);
        try {
            storage.writeTaskData(list);
        } catch (IOException e) {
            ui.showError(e.getMessage());
        }
        System.out.println("added:\n" + event);
    }

    public void deleteTask(Task task, Storage storage, Ui ui) {
        list.remove(task);
        System.out.println("deleted task: " + task);
        try {
            storage.writeTaskData(list);
        } catch (IOException e) {
            ui.showError(e.getMessage());
        }
    }
}
