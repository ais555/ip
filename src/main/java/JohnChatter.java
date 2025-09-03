import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;

public class JohnChatter {
    private static void writeTaskData(File file, ArrayList<Task> items) throws IOException {
        FileWriter writer = new FileWriter(file);
        for (Task item : items) {
            StringBuilder nextLine = new StringBuilder();
            if (item instanceof Todo) {
                nextLine.append("T|")
                        .append(item.isDone ? "1" : "0").append("|")
                        .append(item.description);
            } else if (item instanceof Deadline) {
                Deadline deadline = (Deadline) item;
                nextLine.append("D|")
                        .append(deadline.isDone ? "1" : "0").append("|")
                        .append(deadline.description).append("|")
                        .append(deadline.by);
            } else if (item instanceof Event) {
                Event event = (Event) item;
                nextLine.append("E|")
                        .append(event.isDone ? "1" : "0").append("|")
                        .append(event.description).append("|")
                        .append(event.start).append("|")
                        .append(event.end);
            }
            writer.write(nextLine.toString());
            writer.write(System.lineSeparator());
        }
        writer.close();
    }

    private static ArrayList<Task> loadTaskData(File file) throws IOException {
        ArrayList<Task> items = new ArrayList<>();
        Scanner scanner = new Scanner(file);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();

            String[] parts = line.split("\\|");
            String taskType = parts[0];
            boolean isDone = parts[1].equals("1");

            switch (taskType) {
                case "T":
                    Todo todo = new Todo(parts[2]);
                    if (isDone) todo.markAsDone();
                    items.add(todo);
                    break;
                case "D":
                    Deadline d = new Deadline(parts[2], parts[3]);
                    if (isDone) d.markAsDone();
                    items.add(d);
                    break;
                case "E":
                    Event e = new Event(parts[2], parts[3], parts[4]);
                    if (isDone) e.markAsDone();
                    items.add(e);
                    break;
                default:
                    System.out.println("unrecognised task type");
            }
        }
        return items;
    }

    public static void main(String[] args) throws JohnChatterException, IOException {
        System.out.println("it is i, john chatter");

        File taskData = new File("data/task_data.txt");
        File taskDataParent = taskData.getParentFile();
        if (!taskDataParent.exists()) {
            if (!taskDataParent.mkdirs()) {
                throw new IOException("mkdirs failed");
            };
        }
        if (!taskData.exists()) {
            if (!taskData.createNewFile()) {
                throw new IOException("createNewFile failed");
            };
        }

        ArrayList<Task> items = loadTaskData(taskData);
        String input;
        Scanner scanner = new Scanner(System.in);
        while (true) {
            // Wait for user inputs. Breaks out and returns when the command "bye" is given
            input = scanner.nextLine();
            String[] splitInputAroundSpace = input.split(" ");

            try {
                if (input.equals("bye")) {
                    System.out.println("goodbye!");
                    return;
                } else if (input.equals("list")) {
                    for (int i = 1; i <= items.size(); i++) {
                        Task item = items.get(i -1);
                        if (item != null) {
                            System.out.println(i + "." + item.toString());
                        }
                    }
                } else if (splitInputAroundSpace.length == 2 && splitInputAroundSpace[0].equals("mark") && splitInputAroundSpace[1].matches("\\d+")) {
                    // Mark a task as done
                    int index = Integer.parseInt(splitInputAroundSpace[1]);
                    if (items.get(index - 1) != null) {
                        items.get(index - 1).markAsDone();
                        System.out.println("marked " + items.get(index - 1).description + " as done");
                    } else {
                        System.out.println("i cannot mark a task that doesn't exist yet");
                    }
                } else if (splitInputAroundSpace.length == 2 && splitInputAroundSpace[0].equals("unmark") && splitInputAroundSpace[1].matches("\\d+")) {
                    // Mark a task as undone
                    int index = Integer.parseInt(splitInputAroundSpace[1]);
                    if (items.get(index - 1) != null) {
                        items.get(index - 1).markAsUndone();
                        System.out.println("marked " + items.get(index - 1).description + " as undone");
                    } else {
                        System.out.println("i cannot mark a task that doesn't exist yet");
                    }
                } else if (splitInputAroundSpace[0].equals("todo")) {
                    if (splitInputAroundSpace.length == 1) {
                        throw new JohnChatterException("oops! tasks must have a description");
                    }
                    Todo todo = new Todo(input.split("todo ")[1]);
                    items.add(todo);
                    try {
                        writeTaskData(taskData, items);
                    } catch (IOException e) {
                        System.out.println("something went wrong: " + e.getMessage());
                    }
                    System.out.println("added:\n" + todo.toString());
                } else if (splitInputAroundSpace[0].equals("deadline")) {
                    if (splitInputAroundSpace.length == 1) {
                        throw new JohnChatterException("oops! tasks must have a description");
                    }
                    Deadline deadline = new Deadline(input.split("deadline ")[1].split(" /by")[0], input.split("/by ")[1]);
                    items.add(deadline);
                    try {
                        writeTaskData(taskData, items);
                    } catch (IOException e) {
                        System.out.println("something went wrong: " + e.getMessage());
                    }
                    System.out.println("added:\n" + deadline.toString());
                } else if (splitInputAroundSpace[0].equals("event")) {
                    if (splitInputAroundSpace.length == 1) {
                        throw new JohnChatterException("oops! tasks must have a description");
                    }
                    String end = input.split("/to ")[1];
                    String start = input.split(" /to")[0].split("/from ")[1];
                    String description = input.split(" /to")[0].split(" /from")[0].split("event ")[1];
                    Event event = new Event(description, start, end);
                    items.add(event);
                    try {
                        writeTaskData(taskData, items);
                    } catch (IOException e) {
                        System.out.println("something went wrong: " + e.getMessage());
                    }
                    System.out.println("added:\n" + event.toString());
                } else if (splitInputAroundSpace[0].equals("delete")) {
                    if (splitInputAroundSpace.length == 2 && splitInputAroundSpace[1].matches("\\d+")) {
                        int number = Integer.parseInt(splitInputAroundSpace[1]);
                        String task = items.get(number - 1).toString();
                        items.remove(number - 1);
                        System.out.println("deleted task: " + task);
                        try {
                            writeTaskData(taskData, items);
                        } catch (IOException e) {
                            System.out.println("something went wrong: " + e.getMessage());
                        }
                    }
                } else {
                    throw new JohnChatterException("sorry, i don't know what that means");
                }
            } catch (JohnChatterException e) {
                System.out.println(e.toString());
            }
        }
    }
}
