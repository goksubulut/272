package kt2;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.*;

public class TaskAndWishManagementApp {
    private static final String COMMANDS_FILE = "C:\\Users\\ASUS\\OneDrive\\Desktop\\Commands.txt";
    private static final String TASKS_FILE = "C:\\Users\\ASUS\\OneDrive\\Desktop\\Tasks.txt";
    private static final String WISHES_FILE = "C:\\Users\\ASUS\\OneDrive\\Desktop\\Wishes.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private Map<String, Task> tasks;
    private Map<String, Wish> wishes;
    private Map<String, Child> children;
    
    public TaskAndWishManagementApp() {
        this.tasks = new HashMap<>();
        this.wishes = new HashMap<>();
        this.children = new HashMap<>();
    }
    
    public void start() {
        try {
            loadData();
            processCommands();
            saveData();
        } catch (IOException e) {
            System.err.println("Hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void loadData() throws IOException {
        loadTasks();
        loadWishes();
    }
    
    private void loadTasks() throws IOException {
        File file = new File(TASKS_FILE);
        if (!file.exists()) return;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                	Pattern pattern = Pattern.compile("Task ID: (.*?), Title: '(.*?)', Description: '(.*?)', Start: (.*?) (.*?), End: (.*?) (.*?), Points: (\\d+), Status: (.*?), Rating: ([\\d.]+), Child ID: (.*?), AssignedBy: (.)");
                    Matcher matcher = pattern.matcher(line);
                    
                    if (matcher.find()) {
                        String id = matcher.group(1);
                        String title = matcher.group(2);
                        String description = matcher.group(3);
                        String startDate = matcher.group(4);
                        String startTime = matcher.group(5);
                        String endDate = matcher.group(6);
                        String endTime = matcher.group(7);
                        int points = Integer.parseInt(matcher.group(8));
                        String status = matcher.group(9);
                        double rating = Double.parseDouble(matcher.group(10));
                        String childId = matcher.group(11);
                        char assignedBy = matcher.group(12).charAt(0);
                        
                        Task task = new Task(id, title, description, startDate, startTime, 
                                           endDate, endTime, points, childId, assignedBy);
                        
                        if ("completed".equals(status)) {
                            task.markAsCompleted();
                        }
                        
                        task.setRating(rating);
                        if (rating > 0) {
                            task.setApproved(true);
                        }
                        
                        tasks.put(id, task);
                    }
                } catch (Exception e) {
                    System.err.println("Could not parse task line: " + line);
                }
            }
        }
    }
    
    private void loadWishes() throws IOException {
        File file = new File(WISHES_FILE);
        if (!file.exists()) return;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    Pattern pattern = Pattern.compile("Wish ID: (.*?), Title: '(.*?)', Description: '(.*?)'(?:, Date: (.*?), Time: (.*?))?");
                    Matcher matcher = pattern.matcher(line);
                    
                    if (matcher.find()) {
                        String id = matcher.group(1).replace("Wish ID: ", "");
                        String title = matcher.group(2).replace("Title: ", "").replace("\"", "");
                        String description = matcher.group(3).replace("Description: ", "");
                        
                        Wish wish;
                        if (matcher.group(4) != null && matcher.group(5) != null) {
                            String date = matcher.group(4);
                            String time = matcher.group(5);
                            wish = new Wish(id, title, description, date, time);
                        } else {
                            wish = new Wish(id, title, description);
                        }
                        
                        if (line.contains("Status: Approved")) {
                            wish.setApproved(true);
                        } else if (line.contains("Status: Rejected")) {
                            wish.setApproved(false);
                        }
                        
                        if (line.contains("Required Level:")) {
                            Pattern levelPattern = Pattern.compile("Required Level: (\\d+)");
                            Matcher levelMatcher = levelPattern.matcher(line);
                            if (levelMatcher.find()) {
                                int level = Integer.parseInt(levelMatcher.group(1));
                                wish.setRequiredLevel(level);
                            }
                        }
                        
                        if (line.contains("Rating:")) {
                            Pattern ratingPattern = Pattern.compile("Rating: (\\d+\\.\\d+)");
                            Matcher ratingMatcher = ratingPattern.matcher(line);
                            if (ratingMatcher.find()) {
                                double rating = Double.parseDouble(ratingMatcher.group(1));
                                wish.setRating(rating);
                            }
                        }
                        
                        wishes.put(id, wish);
                    }
                } catch (Exception e) {
                    System.err.println("Could not parse wish line: " + line);
                }
            }
        }
    }
    
    private void processCommands() throws IOException {
        File file = new File(COMMANDS_FILE);
        if (!file.exists()) {
            System.out.println("Command file not found: " + COMMANDS_FILE);
            return;
        }
        
        System.out.println("Processing commands...");
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Processing command: " + line);
                processCommand(line.trim());
            }
        }
    }
    
    private void processCommand(String command) {
        String[] parts = command.split("\\s+", 2);
        String cmd = parts[0];
        String[] params = parts.length > 1 ? parts[1].split("\\s+(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1) : new String[0];
        
        for (int i = 0; i < params.length; i++) {
            params[i] = params[i].replaceAll("^\"|\"$", "");
        }
        
        try {
            switch (cmd) {
                case "ADD_CHILD":
                    processAddChild(params);
                    break;
                case "ADD_TASK1":
                    processAddTask(params);
                    break;
                case "ADD_TASK2":
                    processAddTask2(params);
                    break;
                case "ADD_WISH1":
                case "ADD_WISH2":
                    processAddWish(params);
                    break;
                case "TASK_DONE":
                    processCompleteTask(params);
                    break;
                case "TASK_CHECKED":
                    processTaskChecked(params);
                    break;
                case "WISH_CHECKED":
                    processWishChecked(params);
                    break;
                case "LIST_ALL_TASKS":
                    printAllTasks();
                    break;
                case "LIST_ALL_WISHES":
                    printAllWishes();
                    break;
                case "PRINT_BUDGET":
                    printBudget();
                    break;
                case "PRINT_STATUS":
                    printStatus();
                    break;
                case "DAILY_TASKS":
                    printDailyTasks();
                    break;
                case "WEEKLY_TASKS":
                    printWeeklyTasks();
                    break;
                case "ADD_BUDGET_COIN":
                    processAddBudgetCoin(params);
                    break;
                default:
                    System.err.println("Unknown command: " + cmd);
            }
        } catch (Exception e) {
            System.err.println("Error processing command: " + command);
            System.err.println("Error: " + e.getMessage());
        }
    }
    
    private void processAddTask(String[] params) {
        if (params.length < 7) {
            System.err.println("Insufficient parameters: " + Arrays.toString(params));
            return;
        }

        char assignedBy = params[0].charAt(0); // t or p
        if (assignedBy != 'T' && assignedBy != 'P') {
            System.err.println("Invalid assigner: must be 'T' or 'P'");
            return;
        }
        
        String id = params[1];
        String title = params[2];
        String description = params[3];
        String startDate = params[4];
        String startTime = params[5];

        int points = 10;
        try {
            points = Integer.parseInt(params[6]);
        } catch (NumberFormatException e) {
            System.err.println("Warning: Invalid points value, using default 10");
        }
        String childId = params[7];

        // Child kontrolü
        if (!children.containsKey(childId)) {
            System.err.println("Child ID not found: " + childId);
            return;
        }
      
        Task task = new Task(id, title, description, startDate, startTime, 
                points, childId, assignedBy);

tasks.put(id, task);
System.out.println("Task added: " + task);
}
    
    private void processAddTask2(String[] params) {
        if (params.length < 9) {
            System.err.println("Insufficient parameters: " + Arrays.toString(params));
            return;
        }

        char assignedBy = params[0].charAt(0);
        String id = params[1];
        String title = params[2];
        String description = params[3];
        String startDate = params[4];
        String startTime = params[5];
        String endDate = params[6];
        String endTime = params[7];
        int points = 10;
        try {
            points = Integer.parseInt(params[8]);
        } catch (NumberFormatException e) {
            System.err.println("Warning: Invalid points value, using default 10");
        }
        String childId = params[9]; // 9. parametre childId olmalı (8 points)

        if (!children.containsKey(childId)) {
            System.err.println("Child ID not found: " + childId);
            return;
        }

        Task task = new Task(id, title, description, startDate, startTime, 
                            endDate, endTime, points, childId, assignedBy);
        
        tasks.put(id, task);
        System.out.println("Task added: " + task);
    }
    private void processAddBudgetCoin(String[] params) {
        if (params.length < 2) {
            System.err.println("Insufficient parameters: ADD_BUDGET_COIN <childId> <points>");
            return;
        }
        
        String childId = params[0];
        Child child = children.get(childId);
        if (child == null) {
            System.err.println("Child not found: " + childId);
            return;
        }
        
        try {
            int points = Integer.parseInt(params[1]);
            child.addPoints(points);
            System.out.println("Added " + points + " points to child " + childId);
        } catch (NumberFormatException e) {
            System.err.println("Invalid points value: " + params[1]);
        }
    }
    
    private void processAddWish(String[] params) {
        if (params.length < 3) {
            System.err.println("Insufficient parameters for wish: " + Arrays.toString(params));
            return;
        }
        
        try {
            String id = params[0];
            String title = params[1];
            String description = params[2];
            
            Wish wish;
            if (params.length >= 5) {
                String date = params[3];
                String time = params[4];
                wish = new Wish(id, title, description, date, time);
            } else {
                wish = new Wish(id, title, description);
            }
            wishes.put(id, wish);
            System.out.println("Wish added: " + wish);
        } catch (Exception e) {
            System.err.println("Error adding wish: " + e.getMessage());
        }
    }
    
    private void processAddChild(String[] params) {
        if (params.length < 2) {
            System.err.println("Insufficient parameters for child: " + Arrays.toString(params));
            return;
        }
        
        try {
            String id = params[0];
            String name = params[1];
            
            Child child = new Child(id, name);
            children.put(id, child);
            System.out.println("Child added: " + child);
        } catch (Exception e) {
            System.err.println("Error adding child: " + e.getMessage());
        }
    }
    
    private void processCompleteTask(String[] params) {
        if (params.length < 1) {
            System.err.println("Insufficient parameters for task completion");
            return;
        }
        
        try {
            String taskId = params[0];
            Task task = tasks.get(taskId);
            if (task != null) {
                task.markAsCompleted();
                System.out.println("Task completed: " + task);
            } else {
                System.err.println("Task not found: " + taskId);
            }
        } catch (Exception e) {
            System.err.println("Error completing task: " + e.getMessage());
        }
    }
    
    private void processTaskChecked(String[] params) {
        if (params.length < 2) {
            System.err.println("Insufficient parameters for task check");
            return;
        }
        
        try {
            String taskId = params[0];
            double rating = Double.parseDouble(params[1]);
            Task task = tasks.get(taskId);
            if (task != null) {
                task.setApproved(true);
                task.setRating(rating, this); // this ile app referansını geçiyoruz
                System.out.println("Task checked: " + task);
            } else {
                System.err.println("Task not found: " + taskId);
            }
        } catch (NumberFormatException e) {
            System.err.println("Invalid rating value: " + params[1]);
        } catch (Exception e) {
            System.err.println("Error checking task: " + e.getMessage());
        }
    }
    
    private void processWishChecked(String[] params) {
        if (params.length < 2) {
            System.err.println("Insufficient parameters for wish check");
            return;
        }
        
        try {
            String wishId = params[0];
            boolean approved = params[1].equalsIgnoreCase("APPROVED");
            Wish wish = wishes.get(wishId);
            if (wish != null) {
                wish.setApproved(approved);
                
                // [NEW] Handle rejection by removing from map
                if (!approved) {
                    wishes.remove(wishId);
                    System.out.println("Wish rejected and removed: " + wishId);
                    return;
                }
                
                // [NEW] Set required level if provided
                if (params.length >= 3) {
                    int requiredLevel = Integer.parseInt(params[2]);
                    wish.setRequiredLevel(requiredLevel);
                }
                
                System.out.println("Wish checked: " + wish);
            } else {
                System.err.println("Wish not found: " + wishId);
            }
        } catch (Exception e) {
            System.err.println("Error checking wish: " + e.getMessage());
        }
    }
    
    private void printAllTasks() {
        System.out.println("\nAll Tasks:");
        System.out.println("+------+-------------------+-------------------------------+-----------------------+-----------------------+--------+--------+");
        System.out.println("| ID   | Title             | Description                   | Start                 | End                   | Points | Child  |");
        System.out.println("+------+-------------------+-------------------------------+-----------------------+-----------------------+--------+--------+");
        
        for (Task task : tasks.values()) {
            String startDateTime = String.format("%s %s", task.getStartDate(), task.getStartTime());
            String endDateTime = String.format("%s %s", task.getEndDate(), task.getEndTime());
            
            System.out.printf("| %-4s | %-17s | %-29s | %-21s | %-21s | %-6d | %-6s |\n",
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                startDateTime,
                endDateTime,
                task.getPoints(),
                task.getChildId());
        }
        System.out.println("+------+-------------------+-------------------------------+-----------------------+-----------------------+--------+--------+");
    }

    private void printAllWishes() {
        System.out.println("\nAll Wishes:");
        System.out.println("+------+----------------+--------------------------+----------------+----------------+--------+");
        System.out.println("| ID   | Title          | Description              | Date           | Time           | Status |");
        System.out.println("+------+----------------+--------------------------+----------------+----------------+--------+");
        
        for (Wish wish : wishes.values()) {
            System.out.printf("| %-4s | %-14s | %-24s | %-14s | %-14s | %-6s |\n",
                wish.getId(),
                wish.getTitle(),
                wish.getDescription(),
                wish.getDate() != null ? wish.getDate() : "N/A",
                wish.getTime() != null ? wish.getTime() : "N/A",
                wish.isApproved() ? "Approved" : "Pending");
        }
        System.out.println("+------+----------------+--------------------------+----------------+----------------+--------+");
    }

    private void printDailyTasks() {
    	 LocalDate today = LocalDate.now();
         System.out.println("\nDaily Tasks for " + today + ":");
        System.out.println("+------+-------------------+-------------------------------+-----------------------+-----------------------+--------+--------+");
        System.out.println("| ID   | Title             | Description                   | Start                 | End                   | Points | Child  |");
        System.out.println("+------+-------------------+-------------------------------+-----------------------+-----------------------+--------+--------+");
        
        for (Task task : tasks.values()) {
            try {
                LocalDate taskDate = LocalDate.parse(task.getStartDate(), DATE_FORMATTER);
                if (taskDate.equals(today)) {
                    printTaskRow(task);
                }
            } catch (Exception e) {
                System.err.println("Error parsing date for task " + task.getId());
            }
        }
        System.out.println("+------+-------------------+-------------------------------+-----------------------+-----------------------+--------+--------+");
    }

    private void printWeeklyTasks() {
        LocalDate today = LocalDate.now();
        LocalDate weekEnd = today.plusDays(7);
        System.out.println("\nWeekly Tasks from " + today + " to " + weekEnd + ":");
        System.out.println("+------+-------------------+-------------------------------+-----------------------+-----------------------+--------+--------+");
        System.out.println("| ID   | Title             | Description                   | Start                 | End                   | Points | Child  |");
        System.out.println("+------+-------------------+-------------------------------+-----------------------+-----------------------+--------+--------+");
        
        for (Task task : tasks.values()) {
            try {
                LocalDate taskDate = LocalDate.parse(task.getStartDate(), DATE_FORMATTER);
                if (!taskDate.isBefore(today) && !taskDate.isAfter(weekEnd)) {
                    printTaskRow(task);
                }
            } catch (Exception e) {
                System.err.println("Error parsing date for task " + task.getId());
            }
        }
        System.out.println("+------+-------------------+-------------------------------+-----------------------+-----------------------+--------+--------+");
    }
    
    private void printTaskRow(Task task) {
        String startDateTime = String.format("%s %s", task.getStartDate(), task.getStartTime());
        String endDateTime = String.format("%s %s", task.getEndDate(), task.getEndTime());
        
        System.out.printf("| %-4s | %-17s | %-29s | %-21s | %-21s | %-6d | %-6s |\n",
            task.getId(),
            task.getTitle(),
            task.getDescription(),
            startDateTime,
            endDateTime,
            task.getPoints(),
            task.getChildId());
    }
    private void printBudget() {
        System.out.println("\nBudget Summary:");
        System.out.println("+----------------+----------------+");
        System.out.println("| Category       | Amount        |");
        System.out.println("+----------------+----------------+");
        
        System.out.printf("| %-14s | %-13d |\n", "Total Tasks", tasks.size());
        System.out.printf("| %-14s | %-13d |\n", "Total Wishes", wishes.size());
        System.out.printf("| %-14s | %-13d |\n", "Total Children", children.size());
        
        long approvedWishes = wishes.values().stream()
            .filter(Wish::isApproved)
            .count();
        System.out.printf("| %-14s | %-13d |\n", "Approved Wishes", approvedWishes);
        
        long completedTasks = tasks.values().stream()
            .filter(Task::isCompleted)
            .count();
        System.out.printf("| %-14s | %-13d |\n", "Completed Tasks", completedTasks);
        
        System.out.println("+----------------+----------------+");
    }

    private void printStatus() {
        System.out.println("\nSystem Status:");
        System.out.println("+----------------+----------------+----------------+");
        System.out.println("| Category       | Count         | Status        |");
        System.out.println("+----------------+----------------+----------------+");
        
        long pendingTasks = tasks.values().stream()
            .filter(task -> !task.isCompleted())
            .count();
        System.out.printf("| %-14s | %-13d | %-13s |\n", 
            "Tasks", tasks.size(), 
            pendingTasks + " pending");
        
        long pendingWishes = wishes.values().stream()
            .filter(wish -> !wish.isApproved())
            .count();
        System.out.printf("| %-14s | %-13d | %-13s |\n", 
            "Wishes", wishes.size(), 
            pendingWishes + " pending");
        
        System.out.printf("| %-14s | %-13d | %-13s |\n", 
            "Children", children.size(), 
            "Active");
        
        System.out.println("+----------------+----------------+----------------+");
        
        double avgTaskRating = tasks.values().stream()
            .mapToDouble(Task::getRating)
            .average()
            .orElse(0.0);
        System.out.printf("\nAverage Task Rating: %.1f\n", avgTaskRating);
        
        double avgWishRating = wishes.values().stream()
            .mapToDouble(Wish::getRating)
            .average()
            .orElse(0.0);
        System.out.printf("Average Wish Rating: %.1f\n", avgWishRating);
    }
    
    private void saveData() throws IOException {
        saveTasks();
        saveWishes();
    }
    
    private void saveTasks() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(TASKS_FILE))) {
            for (Task task : tasks.values()) {
                writer.println(task.toString());
            }
        }
    }
    
    private void saveWishes() throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(WISHES_FILE))) {
            for (Wish wish : wishes.values()) {
                writer.println(wish.toString());
            }
        }
    }
    public Child getChildById(String childId) {
        return children.get(childId);
    }
    
    public static void main(String[] args) {
        TaskAndWishManagementApp app = new TaskAndWishManagementApp();
        app.start();
    }
} 