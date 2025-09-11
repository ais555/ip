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
    public String mark(Task task) {
        called = "mark";
        return called;
    }

    @Override
    public String unmark(Task task) {
        called = "unmark";
        return called;
    }

    @Override
    public String addTodo(Todo todo, Storage storage, Ui ui) {
        called = "addTodo";
        return called;
    }

    @Override
    public String addDeadline(Deadline deadline, Storage storage, Ui ui) {
        called = "addDeadline";
        return called;
    }

    @Override
    public String addEvent(Event event, Storage storage, Ui ui) {
        called = "addEvent";
        return called;
    }

    @Override
    public String deleteTask(Task task, Storage storage, Ui ui) {
        called = "deleteTask";
        return called;
    }
}
