package johnchatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    // The following tests were written with the assistance of ChatGPT
    @Test
    public void taskList_addDeadline_success() {
        tasks.addDeadline(new Deadline("submit report", "2025-09-30"), storage, ui);
        assertTrue(tasks.list.get(0).toString().contains("[D][ ] submit report (by:"));
    }

    @Test
    public void taskList_addEvent_success() {
        tasks.addEvent(new Event("meeting", "2025-10-01 10:00", "2025-10-01 11:00"), storage, ui);
        assertEquals("[E][ ] meeting (from: 2025-10-01 10:00 to: 2025-10-01 11:00)",
                tasks.list.get(0).toString());
    }

    @Test
    public void taskList_markTask_success() throws IOException {
        Task todo = new Todo("mark me");
        tasks.list.add(todo);
        tasks.mark(todo, storage);
        assertTrue(todo.isDone);
    }

    @Test
    public void taskList_unmarkTask_success() throws IOException {
        Task todo = new Todo("unmark me");
        todo.markAsDone();
        tasks.list.add(todo);
        tasks.unmark(todo, storage);
        assertFalse(todo.isDone);
    }

    @Test
    public void taskList_deleteTask_invalidTask_doesNothing() {
        Task t1 = new Todo("task one");
        tasks.list.add(t1);
        Task fakeTask = new Todo("not in list");

        tasks.deleteTask(fakeTask, storage, ui);
        assertEquals(1, tasks.list.size());
    }
}
