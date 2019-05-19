package example.jforce.todoapplication.model;

public class TaskReportHelper {
    private String date;
    private int pending, finished, total;

    public TaskReportHelper(String date, int pending, int finished, int total) {
        this.date = date;
        this.pending = pending;
        this.finished = finished;
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public int getPending() {
        return pending;
    }

    public int getFinished() {
        return finished;
    }

    public int getTotal() {
        return total;
    }
}
