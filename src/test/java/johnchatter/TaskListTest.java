package johnchatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskListTest {
    private Ui ui;
    private Storage storage;
    private TaskList tasks;

    @BeforeEach
    public void setup() {
        ui = new Ui();
        storage = new StorageStub();
        tasks = new TaskList(new ArrayList<>());
    }

    @Test
    public void taskList_addTodo_success() {
        tasks.addTodo(new Todo("test"), storage, ui);
        assertEquals("[T][ ] test", tasks.list.get(0).toString());
    }

    @Test
    public void taskList_deleteTask_success() {
        Task stubTask = new Todo("stub");
        tasks.list.add(stubTask);
        tasks.deleteTask(stubTask, storage, ui);
        assertEquals(0, tasks.list.size());
    }
}
