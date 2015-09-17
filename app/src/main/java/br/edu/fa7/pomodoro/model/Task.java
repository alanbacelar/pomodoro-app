package br.edu.fa7.pomodoro.model;

/**
 * Created by alan on 9/12/15.
 */
public class Task {

    private Long id;
    private String title;
    private String description;
    private int tomatoes = 1;
    private int doneTomatoes = 0;
    private boolean done = false;

    public Task(String title, String description, int tomatoes) {
        this.title = title;
        this.description = description;
        this.tomatoes = tomatoes;
    }

    public Task(Long id, String title, String description, int tomatoes) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.tomatoes = tomatoes;
    }

    public Task(Long id) {
        this.id = id;
    }

    public Task() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTomatoes() {
        return tomatoes;
    }

    public void setTomatoes(int tomatoes) {
        this.tomatoes = tomatoes;
    }

    public int getDoneTomatoes() {
        return doneTomatoes;
    }

    public void setDoneTomatoes(int doneTomatoes) {
        this.doneTomatoes = doneTomatoes;
    }

    public  void doneTomatoes(int tomatoes) {
        this.setDoneTomatoes( getDoneTomatoes() + tomatoes );

        if (getDoneTomatoes() >= getTomatoes()) {
            setDone(true);
        }
    }


    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public boolean isValid() {
        if (this.getTomatoes() <= 0) {
            this.setTomatoes(1);
        }

        if (this.getTitle().isEmpty()) {
            return false;
        }

        return true;
    }
}
