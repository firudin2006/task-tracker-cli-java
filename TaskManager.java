import java.io.*;
import java.util.*;

public class TaskManager {
    private static final String FILE_NAME = "tasks.json";
    private List<Task> tasks;

    public TaskManager() {
        tasks = loadTasks();
    }

    public void addTask(String description) {
        int id = nextId();
        Task task = new Task(id, description);
        tasks.add(task);
        saveTasks();
        System.out.println("Task added successfully (ID: " + id + ")");
    }

    public void updateTask(int id, String newDescription) {
        Task task = findTask(id);
        if (task == null) {
            System.out.println("Task with ID " + id + " not found.");
            return;
        }
        task.setDescription(newDescription);
        saveTasks();
        System.out.println("Task " + id + " updated successfully.");
    }

    public void deleteTask(int id) {
        Task task = findTask(id);
        if (task == null) {
            System.out.println("Task with ID " + id + " not found.");
            return;
        }
        tasks.remove(task);
        saveTasks();
        System.out.println("Task " + id + " deleted successfully.");
    }

    public void markTask(int id, String status) {
        Task task = findTask(id);
        if (task == null) {
            System.out.println("Task with ID " + id + " not found.");
            return;
        }
        task.setStatus(status);
        saveTasks();
        System.out.println("Task " + id + " marked as " + status + ".");
    }

    public void listTasks(String filter) {
        List<Task> filtered = new ArrayList<>();
        for (Task t : tasks) {
            if (filter == null || t.getStatus().equalsIgnoreCase(filter)) {
                filtered.add(t);
            }
        }

        if (filtered.isEmpty()) {
            System.out.println("No tasks found for filter: " + (filter == null ? "all" : filter));
            return;
        }

        System.out.println(" ID | Status       | Description (created, updated)");
        System.out.println("-----------------------------------------------------------");
        for (Task t : filtered) {
            System.out.println(t);
        }
    }

    private Task findTask(int id) {
        for (Task t : tasks) {
            if (t.getId() == id) return t;
        }
        return null;
    }

    private int nextId() {
        int max = 0;
        for (Task t : tasks) {
            if (t.getId() > max) max = t.getId();
        }
        return max + 1;
    }

    private List<Task> loadTasks() {
        List<Task> list = new ArrayList<>();
        File file = new File(FILE_NAME);
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String json = br.readLine();
            if (json == null || json.isEmpty()) return list;

            // Very simple parsing (not for production!)
            json = json.trim();
            if (json.startsWith("[") && json.endsWith("]")) {
                json = json.substring(1, json.length() - 1);
                if (!json.trim().isEmpty()) {
                    String[] items = json.split("},\\{");
                    for (String item : items) {
                        String fixed = item.replace("{", "").replace("}", "");
                        Map<String, String> map = new HashMap<>();
                        for (String kv : fixed.split(",\"")) {
                            String[] parts = kv.replace("\"", "").split(":", 2);
                            if (parts.length == 2)
                                map.put(parts[0], parts[1]);
                        }
                        try {
                            int id = Integer.parseInt(map.get("id"));
                            Task t = new Task(id, map.get("description"));
                            t.setStatus(map.get("status"));
                            list.add(t);
                        } catch (Exception e) {
                            // Skip malformed entries
                        }
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading tasks: " + e.getMessage());
        }
        return list;
    }

    private void saveTasks() {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NAME))) {
            pw.println("[" + String.join(",", tasks.stream().map(Task::toJson).toList()) + "]");
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }
}
