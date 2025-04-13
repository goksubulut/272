package kt2;

public class Wish {
    private String id;
    private String title;
    private String description;
    private String status; // "Pending", "Approved", "Rejected"
    private int requiredLevel;

    // Constructor
    public Wish(String id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.status = "Pending";
        this.requiredLevel = 0;
    }

    public void approve(int level) {
        this.status = "Approved";
        this.requiredLevel = level;
    }

    public void reject() {
        this.status = "Rejected";
    }

    @Override
    public String toString() {
        return "Wish ID: " + id + ", Title: '" + title + "', Status: " + status + ", Required Level: " + requiredLevel;
    }
}
