package kt2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TaskAndWishManagementApp {
    private static Child child;

    public static void main(String[] args) {
        child = new Child("John");
        readTasks();
        readWishes();
        executeCommands("C:\\Users\\ASUS\\OneDrive\\Desktop\\commands.txt");
    }

    public static void readTasks() {
        child.addTask(new Task("101", "Math Homework", "Solve pages 10 to 20", "2025-03-01", "15:00", 10));
        child.addTask(new Task("102", "School Picnic Preparation", "Göksu Park", "2025-03-05", "10:00", 15));
    }

    public static void readWishes() {
        child.addWish(new Wish("W101", "Lego Set", "Price:150TL, Brand:LEGO"));
        child.addWish(new Wish("W102", "Go to the Cinema", "Price:100TL"));
    }

    public static void executeCommands(String commandsFilePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(commandsFilePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] command = line.split(" ");
                switch (command[0]) {
                    case "ADD_TASK1":
                        addTask(command);
                        break;
                    case "LIST_ALL_TASKS":
                        child.printTasks();
                        break;
                    case "ADD_WISH1":
                        addWish(command);
                        break;
                    case "LIST_ALL_WISHES":
                        child.printWishes();
                        break;
                    case "TASK_DONE":
                        markTaskDone(command[1]);
                        break;
                    case "PRINT_BUDGET":
                        System.out.println("Current Points: " + child.getPoints());
                        break;
                    default:
                        System.out.println("Unknown command: " + command[0]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void addTask(String[] command) {
        String id = command[2];
        String title = command[3].replace("\"", "");
        String description = command[4].replace("\"", "");
        String dueDate = command[5];
        String dueTime = command[6];
        int points = Integer.parseInt(command[7]);
        Task task = new Task(id, title, description, dueDate, dueTime, points);
        child.addTask(task);
        System.out.println("Task added: " + task);
    }

    public static void addWish(String[] command) {
        String id = command[1];
        String title = command[2].replace("\"", "");
        String description = command[3].replace("\"", "");
        Wish wish = new Wish(id, title, description);
        child.addWish(wish);
        System.out.println("Wish added: " + wish);
    }

    public static void markTaskDone(String taskId) {
        for (Task task : child.tasks) {
            if (task.id.equals(taskId)) {
                task.markAsCompleted();
                System.out.println("Task marked as done: " + task);
                return;
            }
        }
        System.out.println("Task not found: " + taskId);
    }
}

