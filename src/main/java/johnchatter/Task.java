package johnchatter;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents a task on the user's list
 */
public class Task {
    protected String description;
    protected boolean isDone;
    protected Set<String> tags;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
        this.tags = new HashSet<>();
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsUndone() {
        this.isDone = false;
    }

    public void addTag(String tag) {
        tags.add(tag);
    }

    public void removeTag(String tag) {
        tags.remove(tag);
    }

    public Set<String> getTags() {
        return tags;
    }

    @Override
    public String toString() {
        String tagString = tags.isEmpty() ? "" : " " + tags.toString();
        return "[" + this.getStatusIcon() + "] " + this.description + tagString;
    }
}
