package kidtask;

import java.time.LocalDateTime;

public class Task {
    private int id;
    private String title;
    private String description;
    private LocalDateTime deadline;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private int points;
    private boolean isCompleted;
    private boolean isApproved;
    private int rating;
    private char assignedBy; // 'T' for Teacher, 'F' for Parent

    public Task(int id, String title, String description, LocalDateTime deadline, int points, char assignedBy) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.points = points;
        this.assignedBy = assignedBy;
        this.isCompleted = false;
        this.isApproved = false;
        this.rating = 0;
    }

    // Getters and Setters
    public int getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getDeadline() { return deadline; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public int getPoints() { return points; }
    public boolean isCompleted() { return isCompleted; }
    public boolean isApproved() { return isApproved; }
    public int getRating() { return rating; }
    public char getAssignedBy() { return assignedBy; }

    public void setCompleted(boolean completed) { isCompleted = completed; }
    public void setApproved(boolean approved) { isApproved = approved; }
    public void setRating(int rating) { this.rating = rating; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    @Override
    public String toString() {
        return String.format("Task ID: %d, Title: %s, Description: %s, Deadline: %s, Points: %d, Completed: %b, Approved: %b, Rating: %d",
                id, title, description, deadline, points, isCompleted, isApproved, rating);
    }
} 