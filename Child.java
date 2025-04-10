package kidtask;

public class Child {
    private int id;
    private String name;
    private int points;
    private int level;
    private double averageRating;

    public Child(int id, String name) {
        this.id = id;
        this.name = name;
        this.points = 0;
        this.level = 1;
        this.averageRating = 0.0;
    }

    public void addPoints(int points) {
        this.points += points;
        updateLevel();
    }

    public void updateRating(double newRating) {
        if (averageRating == 0.0) {
            averageRating = newRating;
        } else {
            averageRating = (averageRating + newRating) / 2.0;
        }
        updateLevel();
    }

    private void updateLevel() {
        if (averageRating >= 80) {
            level = 4;
        } else if (averageRating >= 60) {
            level = 3;
        } else if (averageRating >= 40) {
            level = 2;
        } else {
            level = 1;
        }
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public int getPoints() { return points; }
    public int getLevel() { return level; }
    public double getAverageRating() { return averageRating; }

    @Override
    public String toString() {
        return String.format("Child ID: %d, Name: %s, Points: %d, Level: %d, Average Rating: %.2f",
                id, name, points, level, averageRating);
    }
} 