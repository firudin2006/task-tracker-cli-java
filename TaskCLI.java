public class TaskCLI {
    public static void main(String[] args) {
        if (args.length < 1) {
            printHelp();
            return;
        }

        TaskManager manager = new TaskManager();
        String command = args[0].toLowerCase();

        switch (command) {
            case "add" -> {
                if (args.length < 2) {
                    System.out.println("Usage: java TaskCLI add \"description\"");
                    return;
                }
                String desc = String.join(" ", java.util.Arrays.copyOfRange(args, 1, args.length));
                manager.addTask(desc);
            }
            case "update" -> {
                if (args.length < 3) {
                    System.out.println("Usage: java TaskCLI update <id> \"new description\"");
                    return;
                }
                try {
                    int id = Integer.parseInt(args[1]);
                    String desc = String.join(" ", java.util.Arrays.copyOfRange(args, 2, args.length));
                    manager.updateTask(id, desc);
                } catch (NumberFormatException e) {
                    System.out.println("Task ID must be an integer.");
                }
            }
            case "delete" -> {
                if (args.length < 2) {
                    System.out.println("Usage: java TaskCLI delete <id>");
                    return;
                }
                try {
                    int id = Integer.parseInt(args[1]);
                    manager.deleteTask(id);
                } catch (NumberFormatException e) {
                    System.out.println("Task ID must be an integer.");
                }
            }
            case "mark-in-progress" -> mark(manager, args, "in-progress");
            case "mark-done" -> mark(manager, args, "done");
            case "list" -> {
                if (args.length == 2)
                    manager.listTasks(args[1]);
                else
                    manager.listTasks(null);
            }
            case "help" -> printHelp();
            default -> System.out.println("Unknown command. Use 'help' to see available commands.");
        }
    }

    private static void mark(TaskManager manager, String[] args, String status) {
        if (args.length < 2) {
            System.out.println("Usage: java TaskCLI " + (status.equals("in-progress") ? "mark-in-progress" : "mark-done") + " <id>");
            return;
        }
        try {
            int id = Integer.parseInt(args[1]);
            manager.markTask(id, status);
        } catch (NumberFormatException e) {
            System.out.println("Task ID must be an integer.");
        }
    }

    private static void printHelp() {
        System.out.println("""
        Task Tracker CLI - Commands:
          add "description"            Add a new task
          update <id> "desc"           Update an existing task
          delete <id>                  Delete a task
          mark-in-progress <id>        Mark a task as in progress
          mark-done <id>               Mark a task as done
          list [status]                List tasks (all/todo/in-progress/done)
        """);
    }
}
