package johnchatter;

import java.util.ArrayList;

public class TaskListStub extends TaskList {
    public String called;

    public TaskListStub() {
        super(new ArrayList<>());
        Task stubTask = new Todo("stub");
        this.list.add(stubTask);
    }

    @Override
    public void mark(Task task) {
        called = "mark";
    }

    @Override
    public void unmark(Task task) {
        called = "unmark";
    }

    @Override
    public void addTodo(Todo todo, Storage storage, Ui ui) {
        called = "addTodo";
    }

    @Override
    public void addDeadline(Deadline deadline, Storage storage, Ui ui) {
        called = "addDeadline";
    }

    @Override
    public void addEvent(Event event, Storage storage, Ui ui) {
        called = "addEvent";
    }

    @Override
    public void deleteTask(Task task, Storage storage, Ui ui) {
        called = "deleteTask";
    }
}
