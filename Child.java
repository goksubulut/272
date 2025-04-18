package kt2;

public class Child {
    private String id;
    private String name;
    private int totalPoints;
    private int level;
    private double averagePoints;
    private int completedTaskCount;

    public Child(String id, String name) {
        this.id = id;
        this.name = name;
        this.totalPoints = 0;
        this.level = 1;
        this.averagePoints = 0;
        this.completedTaskCount = 0;
    }
    
    public void addPoints(int points) {
        this.totalPoints += points;
        updateLevel();
    }

    public void addTaskPoints(int basePoints, double rating) {
        double earnedPoints = basePoints * (rating / 5.0);
        this.totalPoints += (int) earnedPoints;
        this.completedTaskCount++;
        this.averagePoints = (double) totalPoints / completedTaskCount;
        updateLevel();
    }

    private void updateLevel() {
        if (averagePoints >= 81) {
            level = 4;
        } else if (averagePoints >= 61) {
            level = 3;
        } else if (averagePoints >= 41) {
            level = 2;
        } else {
            level = 1;
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getTotalPoints() {
        return totalPoints;
    }

    public int getLevel() {
        return level;
    }

    public double getAveragePoints() {
        return averagePoints;
    }
    
    @Override
    public String toString() {
        return String.format("Child ID: %s, Name: %s, Total Points: %d, Level: %d, Average Points: %.1f",
                id, name, totalPoints, level, averagePoints);
    }
}