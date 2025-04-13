package kt2;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Task {
    String id;
    private String title;
    private String description;
    private String dueDate;
    private String dueTime;
    private int points;
    private String status; // "Pending" or "Completed"
    private int rating; // 1-5

    // Constructor
    public Task(String id, String title, String description, String dueDate, String dueTime, int points) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.dueTime = dueTime;
        this.points = points;
        this.status = "Pending";
        this.rating = 0;
    }

    public void markAsCompleted() {
        this.status = "Completed";
    }

    public void rate(int rating) {
        if (rating >= 1 && rating <= 5) {
            this.rating = rating;
        }
    }

    @Override
    public String toString() {
        return "Task ID: " + id + ", Title: '" + title + "', Status: " + status + ", Points: " + points + ", Rating: " + rating;
    }
}
