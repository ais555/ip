package johnchatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class ParserTest {
    private Ui ui;
    private Storage storage;
    private TaskListStub tasks;

    @BeforeEach
    public void setup() {
        ui = new Ui();
        storage = new StorageStub();
        tasks = new TaskListStub();
    }

    @Test
    public void parse_addTodo_success() throws JohnChatterException {
        Parser.parse("todo test", ui, storage, tasks);
        assertEquals("addTodo", tasks.called);
    }

    @Test
    public void parse_addTodo_exceptionThrown() {
        try {
            Parser.parse("todo", ui, storage, tasks);
            fail();
        } catch (Exception e) {
            assertEquals("oops! tasks must have a description", e.getMessage());
        }
    }

    @Test
    public void parse_markTask_success() throws JohnChatterException {
        Parser.parse("mark 1", ui, storage, tasks);
        assertEquals("mark", tasks.called);
    }

    @Test
    public void parse_markTask_exceptionThrown() {
        try {
            Parser.parse("mark 2", ui, storage, tasks);
            fail();
        } catch (Exception e) {
            assertEquals("it seems you have input an invalid task number, please check and try again",
                    e.getMessage());
        }
    }

    @Test
    public void parse_deleteTask_success() throws JohnChatterException {
        Parser.parse("delete 1", ui, storage, tasks);
        assertEquals("deleteTask", tasks.called);
    }

    @Test
    public void parse_deleteTask_exceptionThrown() {
        try {
            Parser.parse("delete 2", ui, storage, tasks);
            fail();
        } catch (Exception e) {
            assertEquals("it seems you have input an invalid task number, please check and try again",
                    e.getMessage());
        }
    }

    @Test
    public void parse_unknownCommand() {
        try {
            Parser.parse("foo", ui, storage, tasks);
            fail();
        } catch (Exception e) {
            assertEquals("sorry, i don't know what that means", e.getMessage());
        }
    }
}
