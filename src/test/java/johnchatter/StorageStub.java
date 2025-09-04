package johnchatter;

import java.io.IOException;
import java.util.ArrayList;

public class StorageStub extends Storage {
    private ArrayList<Task> list = new ArrayList<>();

    public StorageStub() {
        super("stub");
    }

    @Override
    public void writeTaskData(ArrayList<Task> items) throws IOException {
        list = items;
    }

    @Override
    public ArrayList<Task> loadTaskData() throws IOException {
        return list;
    }

    @Override
    public ArrayList<Task> load() throws IOException {
        return loadTaskData();
    }
}
