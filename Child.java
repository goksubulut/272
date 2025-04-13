package kt2;

import java.util.ArrayList;
import java.util.List;

public class Child {
    private String name;
    private int points;
    List<Task> tasks;
    private List<Wish> wishes;

    public Child(String name) {
        this.name = name;
        this.points = 0;
        this.tasks = new ArrayList<>();
        this.wishes = new ArrayList<>();
    }

    public void addTask(Task task) {
        tasks.add(task);
    }

    public void addWish(Wish wish) {
        wishes.add(wish);
    }

    public void printTasks() {
        for (Task task : tasks) {
            System.out.println(task);
        }
    }

    public void printWishes() {
        for (Wish wish : wishes) {
            System.out.println(wish);
        }
    }

    public void updatePoints(int points) {
        this.points += points;
    }

    public int getPoints() {
        return points;
    }
}
