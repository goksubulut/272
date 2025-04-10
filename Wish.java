package kidtask;

import java.time.LocalDateTime;

public class Wish {
    private String id;
    private String title;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isApproved;
    private int requiredLevel;
    private boolean isActivity;

    public Wish(String id, String title, String description, boolean isActivity) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.isActivity = isActivity;
        this.isApproved = false;
        this.requiredLevel = 1;
    }

    // Getters and Setters
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public boolean isApproved() { return isApproved; }
    public int getRequiredLevel() { return requiredLevel; }
    public boolean isActivity() { return isActivity; }

    public void setApproved(boolean approved) { isApproved = approved; }
    public void setRequiredLevel(int level) { this.requiredLevel = level; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    @Override
    public String toString() {
        String base = String.format("Wish ID: %s, Title: %s, Description: %s, Approved: %b, Required Level: %d",
                id, title, description, isApproved, requiredLevel);
        if (isActivity && startTime != null && endTime != null) {
            base += String.format(", Start Time: %s, End Time: %s", startTime, endTime);
        }
        return base;
    }
} 