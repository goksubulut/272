package kt2;

public class Task {
    private String id;
    private String title;
    private String description;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private int points;
    private boolean isCompleted;
    private boolean isApproved;
    private double rating;
    private String childId;
    private char assignedBy; // 'T' for Teacher, 'P' for Family/Parent

    // Constructor for tasks with start and end dates
    public Task(String id, String title, String description, String startDate, 
            String startTime, String endDate, String endTime, int points, 
            String childId, char assignedBy) { // assignedBy char yapıldı
     this.id = id;
     this.title = title;
     this.description = description;
     this.startDate = startDate;
     this.startTime = startTime;
     this.endDate = endDate;
     this.endTime = endTime;
     this.points = points;
     this.isCompleted = false;
     this.isApproved = false;
     this.rating = 0.0;
     this.childId = childId;
     this.assignedBy = assignedBy;
 }


    // Constructor for tasks with only start date
    public Task(String id, String title, String description, String startDate, 
            String startTime, int points, String childId, char assignedBy) { // assignedBy char yapıldı
     this(id, title, description, startDate, startTime, startDate, startTime, 
          points, childId, assignedBy);
 }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getEndTime() {
        return endTime;
    }

    public int getPoints() {
        return points;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public double getRating() {
        return rating;
    }

    public String getChildId() {
        return childId;
    }
    public char getAssignedBy() {
        return assignedBy;
    }
    public void markAsCompleted() {
        this.isCompleted = true;
    }

    public void setApproved(boolean approved) {
        this.isApproved = approved;
        		
    }

    public void setRating(double rating) {
        this.rating = rating;
        if (rating > 0) {
            this.isApproved = true;
        }
    }

    public void setRating(double rating, TaskAndWishManagementApp app) {
        setRating(rating); // Call the single-parameter version
        if (rating > 0 && app != null) {
            // Çocuğa puan ekle
            Child child = app.getChildById(childId);
            if (child != null) {
                child.addTaskPoints(points, rating);
            }
        }
    }
    @Override
    public String toString() {
        return String.format("Task ID: %s, Title: '%s', Description: '%s', Start: %s %s, End: %s %s, Points: %d, Status: %s, Rating: %.1f, Child ID: %s, AssignedBy: %c",
            id, title, description, startDate, startTime, endDate, endTime, points,
            isCompleted ? "completed" : "notcompleted", rating, childId, assignedBy);
    }
}