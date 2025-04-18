package kt2;

public class Wish {
    private String id;
    private String title;
    private String description;
    private String date;
    private String time;
    private String status;
    private int requiredLevel;
    private boolean isApproved;
    private double rating;

    public Wish(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = "Pending";
        this.requiredLevel = 0;
        this.isApproved = false;
        this.rating = 0.0;
    }

    public Wish(String id, String title, String description, String date, String time) {
        this(id, title, description);
        this.date = date;
        this.time = time;
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

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public int getRequiredLevel() {
        return requiredLevel;
    }

    public double getRating() {
        return rating;
    }

    public void setApproved(boolean approved) {
        this.isApproved = approved;
        this.status = approved ? "Approved" : "Rejected";
    }

    public void setRequiredLevel(int level) {
        this.requiredLevel = level;
    }
    public boolean isRejected() {
        return "Rejected".equals(status);
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
    public boolean canBeGranted(String childId, TaskAndWishManagementApp app) {
        Child child = app.getChildById(childId);
        if (child == null) return false;
        return child.getLevel() >= requiredLevel;
    }

    @Override
    public String toString() {
        String base = String.format("Wish ID: %s, Title: '%s', Description: '%s', Status: %s, Required Level: %d, Rating: %.1f",
                id, title, description, status, requiredLevel, rating);
        
        if (date != null && time != null) {
            return base + String.format(", Date: %s, Time: %s", date, time);
        }
        return base;
    }
}  