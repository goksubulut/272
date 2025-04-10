package kidtask;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class KidTaskApp {
    private TaskManager taskManager;
    private WishManager wishManager;
    private Map<Integer, Child> children;
    private static final String COMMANDS_FILE = "src/Command.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    public KidTaskApp() {
        System.out.println("KidTaskApp başlatılıyor...");
        taskManager = new TaskManager();
        wishManager = new WishManager();
        children = new HashMap<>();
        children.put(1, new Child(1, "Default Child")); // Default child for testing
        System.out.println("KidTaskApp başlatıldı.");
    }

    public void executeCommands() {
        System.out.println("Komutlar çalıştırılıyor...");
        System.out.println("Komut dosyası: " + COMMANDS_FILE);
        
        File commandFile = new File(COMMANDS_FILE);
        if (!commandFile.exists()) {
            System.out.println("HATA: Komut dosyası bulunamadı: " + COMMANDS_FILE);
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(COMMANDS_FILE))) {
            String line;
            int lineNumber = 0;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                System.out.println("\nKomut #" + lineNumber + ": " + line);
                processCommand(line.trim());
            }
            System.out.println("\nTüm komutlar başarıyla çalıştırıldı.");
        } catch (IOException e) {
            System.out.println("HATA: Komut dosyası okunurken hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void processCommand(String command) {
        String[] parts = command.split("\\s+");
        if (parts.length == 0) return;

        switch (parts[0]) {
            case "ADD_TASK":
                processAddTask(parts);
                break;
            case "LIST_ALL_TASKS":
                processListTasks(parts);
                break;
            case "LIST_ALL_WISHES":
                processListWishes();
                break;
            case "TASK_DONE":
                processTaskDone(parts);
                break;
            case "TASK_CHECKED":
                processTaskChecked(parts);
                break;
            case "ADD_WISH":
                processAddWish(parts);
                break;
            case "ADD_BUDGET_COIN":
                processAddBudgetCoin(parts);
                break;
            case "WISH_CHECKED":
                processWishChecked(parts);
                break;
            case "PRINT_BUDGET":
                processPrintBudget();
                break;
            case "PRINT_STATUS":
                processPrintStatus();
                break;
            default:
                System.out.println("Bilinmeyen komut: " + parts[0]);
        }
    }

    private void processAddTask(String[] parts) {
        if (parts.length < 7) {
            System.out.println("HATA: ADD_TASK komutu için yetersiz parametre");
            return;
        }
        try {
            char assignedBy = parts[1].charAt(0);
            int taskId = Integer.parseInt(parts[2]);
            String title = parts[3].replace("\"", "");
            String description = parts[4].replace("\"", "");
            LocalDateTime deadline = LocalDateTime.parse(parts[5] + " " + parts[6], 
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            int points = Integer.parseInt(parts[7]);

            Task task = new Task(taskId, title, description, deadline, points, assignedBy);
            taskManager.addTask(task);
            System.out.println("Görev eklendi: " + task.getTitle());
        } catch (Exception e) {
            System.out.println("HATA: Görev eklenirken hata oluştu: " + e.getMessage());
        }
    }

    private void processListTasks(String[] parts) {
        if (parts.length > 1) {
            switch (parts[1]) {
                case "D":
                    taskManager.getDailyTasks().forEach(System.out::println);
                    break;
                case "W":
                    taskManager.getWeeklyTasks().forEach(System.out::println);
                    break;
            }
        } else {
            taskManager.getAllTasks().forEach(System.out::println);
        }
    }

    private void processListWishes() {
        wishManager.getAllWishes().forEach(System.out::println);
    }

    private void processTaskDone(String[] parts) {
        if (parts.length < 2) return;
        int taskId = Integer.parseInt(parts[1]);
        Task task = taskManager.getTaskById(taskId);
        if (task != null) {
            task.setCompleted(true);
            taskManager.saveTasks();
        }
    }

    private void processTaskChecked(String[] parts) {
        if (parts.length < 3) return;
        int taskId = Integer.parseInt(parts[1]);
        int rating = Integer.parseInt(parts[2]);
        Task task = taskManager.getTaskById(taskId);
        if (task != null) {
            task.setApproved(true);
            task.setRating(rating);
            Child child = children.get(1); // Using default child
            child.addPoints(task.getPoints());
            child.updateRating(rating);
            taskManager.saveTasks();
        }
    }

    private void processAddWish(String[] parts) {
        if (parts.length < 4) return;
        String wishId = parts[1];
        String title = parts[2].replace("\"", "");
        String description = parts[3].replace("\"", "");
        Wish wish = new Wish(wishId, title, description, false);
        wishManager.addWish(wish);
    }

    private void processAddBudgetCoin(String[] parts) {
        if (parts.length < 2) return;
        int points = Integer.parseInt(parts[1]);
        Child child = children.get(1); // Using default child
        child.addPoints(points);
    }

    private void processWishChecked(String[] parts) {
        if (parts.length < 3) return;
        String wishId = parts[1];
        boolean approved = parts[2].equals("APPROVED");
        Wish wish = wishManager.getWishById(wishId);
        if (wish != null) {
            wish.setApproved(approved);
            if (approved && parts.length > 3) {
                int requiredLevel = Integer.parseInt(parts[3]);
                wish.setRequiredLevel(requiredLevel);
            }
            wishManager.saveWishes();
        }
    }

    private void processPrintBudget() {
        Child child = children.get(1); // Using default child
        System.out.println("Current Budget: " + child.getPoints() + " points");
    }

    private void processPrintStatus() {
        Child child = children.get(1); // Using default child
        System.out.println(child.toString());
    }

    public static void main(String[] args) {
        System.out.println("KidTask uygulaması başlatılıyor...");
        KidTaskApp app = new KidTaskApp();
        app.executeCommands();
        System.out.println("KidTask uygulaması tamamlandı.");
    }
} 