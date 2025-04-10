package kidtask;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class TaskManager {
    private List<Task> tasks;
    private static final String TASKS_FILE = "src/Task.txt";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public TaskManager() {
        System.out.println("TaskManager başlatılıyor...");
        tasks = new ArrayList<>();
        loadTasks();
        System.out.println("TaskManager başlatıldı. Toplam görev sayısı: " + tasks.size());
    }

    private void loadTasks() {
        System.out.println("Görevler yükleniyor...");
        File tasksFile = new File(TASKS_FILE);
        if (!tasksFile.exists()) {
            System.out.println("UYARI: Görev dosyası bulunamadı. Yeni dosya oluşturulacak.");
            try {
                tasksFile.createNewFile();
            } catch (IOException e) {
                System.out.println("HATA: Görev dosyası oluşturulamadı: " + e.getMessage());
            }
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(TASKS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\s+");
                if (parts.length >= 7) {
                    String taskType = parts[0];
                    char assignedBy = parts[1].charAt(0);
                    String taskId = parts[2];
                    String title = parts[3].replace("\"", "");
                    String description = parts[4].replace("\"", "");
                    LocalDateTime deadline = LocalDateTime.parse(parts[5] + " " + parts[6], DATE_FORMATTER);
                    int points = Integer.parseInt(parts[7]);
                    
                    Task task = new Task(Integer.parseInt(taskId.substring(1)), title, description, deadline, points, assignedBy);
                    
                    // Eğer TASK2 ise ve başlangıç/bitiş zamanları varsa
                    if (taskType.equals("TASK2") && parts.length >= 9) {
                        LocalDateTime startTime = LocalDateTime.parse(parts[7] + " " + parts[8], DATE_FORMATTER);
                        LocalDateTime endTime = LocalDateTime.parse(parts[9] + " " + parts[10], DATE_FORMATTER);
                        task.setStartTime(startTime);
                        task.setEndTime(endTime);
                    }
                    
                    tasks.add(task);
                    System.out.println("Görev yüklendi: " + task.getTitle());
                }
            }
        } catch (IOException e) {
            System.out.println("HATA: Görevler yüklenirken hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void saveTasks() {
        System.out.println("Görevler kaydediliyor...");
        try (PrintWriter writer = new PrintWriter(new FileWriter(TASKS_FILE))) {
            for (Task task : tasks) {
                StringBuilder line = new StringBuilder();
                
                // TASK1 veya TASK2 belirleme
                if (task.getStartTime() != null && task.getEndTime() != null) {
                    line.append("TASK2 ");
                } else {
                    line.append("TASK1 ");
                }
                
                line.append(task.getAssignedBy()).append(" ")
                    .append("T").append(task.getId()).append(" ")
                    .append("\"").append(task.getTitle()).append("\" ")
                    .append("\"").append(task.getDescription()).append("\" ")
                    .append(task.getDeadline().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append(" ")
                    .append(task.getDeadline().format(DateTimeFormatter.ofPattern("HH:mm"))).append(" ");
                
                // Eğer başlangıç/bitiş zamanları varsa
                if (task.getStartTime() != null && task.getEndTime() != null) {
                    line.append(task.getStartTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append(" ")
                        .append(task.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm"))).append(" ")
                        .append(task.getEndTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).append(" ")
                        .append(task.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm"))).append(" ");
                }
                
                line.append(task.getPoints());
                
                writer.println(line.toString());
                System.out.println("Görev kaydedildi: " + task.getTitle());
            }
            System.out.println("Tüm görevler başarıyla kaydedildi.");
        } catch (IOException e) {
            System.out.println("HATA: Görevler kaydedilirken hata oluştu: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks);
    }

    public List<Task> getDailyTasks() {
        List<Task> dailyTasks = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        for (Task task : tasks) {
            if (task.getDeadline().toLocalDate().equals(now.toLocalDate())) {
                dailyTasks.add(task);
            }
        }
        return dailyTasks;
    }

    public List<Task> getWeeklyTasks() {
        List<Task> weeklyTasks = new ArrayList<>();
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekEnd = now.plusDays(7);
        for (Task task : tasks) {
            if (task.getDeadline().isAfter(now) && task.getDeadline().isBefore(weekEnd)) {
                weeklyTasks.add(task);
            }
        }
        return weeklyTasks;
    }

    public Task getTaskById(int id) {
        for (Task task : tasks) {
            if (task.getId() == id) {
                return task;
            }
        }
        return null;
    }

    public void addTask(Task task) {
        tasks.add(task);
        saveTasks();
    }
}
