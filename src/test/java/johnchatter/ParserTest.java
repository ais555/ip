package johnchatter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

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
    public void parse_addTodo_success() throws JohnChatterException, IOException {
        Parser.parse("todo test", ui, storage, tasks);
        assertEquals("addTodo", tasks.called);
    }

    @Test
    public void parse_addTodo_exceptionThrown() {
        try {
            Parser.parse("todo", ui, storage, tasks);
            fail();
        } catch (Exception e) {
            assertEquals("oops! todo must have a description", e.getMessage());
        }
    }

    @Test
    public void parse_markTask_success() throws JohnChatterException, IOException {
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
    public void parse_deleteTask_success() throws JohnChatterException, IOException {
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

    // ChatGPT was used to write the following tests

    @Test
    public void parse_unmarkTask_success() throws JohnChatterException, IOException {
        Parser.parse("unmark 1", ui, storage, tasks);
        assertEquals("unmark", tasks.called);
    }

    @Test
    public void parse_unmarkTask_exceptionThrown() {
        try {
            Parser.parse("unmark 2", ui, storage, tasks);
            fail();
        } catch (Exception e) {
            assertEquals("it seems you have input an invalid task number, please check and try again",
                    e.getMessage());
        }
    }

    @Test
    public void parse_addDeadline_success() throws JohnChatterException, IOException {
        Parser.parse("deadline assignment /by 2025-09-21", ui, storage, tasks);
        assertEquals("addDeadline", tasks.called);
    }

    @Test
    public void parse_addDeadline_exceptionThrown() {
        try {
            Parser.parse("deadline assignment", ui, storage, tasks);
            fail();
        } catch (Exception e) {
            assertEquals("oops! deadline must have a /by date", e.getMessage());
        }
    }

    @Test
    public void parse_addEvent_success() throws JohnChatterException, IOException {
        Parser.parse("event party /from 2025-09-21 /to 2025-09-22", ui, storage, tasks);
        assertEquals("addEvent", tasks.called);
    }

    @Test
    public void parse_addEvent_exceptionThrown() {
        try {
            Parser.parse("event party /from 2025-09-21", ui, storage, tasks);
            fail();
        } catch (Exception e) {
            assertEquals("oops! event must have /from and /to dates", e.getMessage());
        }
    }

    @Test
    public void parse_list_success() throws JohnChatterException, IOException {
        String result = Parser.parse("list", ui, storage, tasks);
        // should contain stub task
        assertEquals("1.[T][ ] stub\n", result);
    }

    @Test
    public void parse_find_singleKeyword_success() throws JohnChatterException, IOException {
        String result = Parser.parse("find stub", ui, storage, tasks);
        assertEquals("1.[T][ ] stub\n", result);
    }

    @Test
    public void parse_find_multipleKeywords_noMatch() throws JohnChatterException, IOException {
        String result = Parser.parse("find stub project", ui, storage, tasks);
        assertEquals("", result); // no task contains both keywords
    }

    @Test
    public void parse_find_noMatch() throws JohnChatterException, IOException {
        String result = Parser.parse("find nothing", ui, storage, tasks);
        assertEquals("", result);
    }
}
