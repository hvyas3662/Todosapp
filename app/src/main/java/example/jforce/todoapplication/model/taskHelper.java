package example.jforce.todoapplication.model;

/**
 * Created by harshal on 5/18/2019.
 */

public class taskHelper {
    private String id, task, user_id, date, STATUS;

    public taskHelper(String id, String task, String user_id, String date, String STATUS) {
        this.id = id;
        this.task = task;
        this.user_id = user_id;
        this.date = date;
        this.STATUS = STATUS;
    }

    public String getId() {
        return id;
    }

    public String getTask() {
        return task;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getDate() {
        return date;
    }

    public String getStaus() {
        return STATUS;
    }
}
