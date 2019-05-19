package example.jforce.todoapplication.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import example.jforce.todoapplication.R;
import example.jforce.todoapplication.adapters.TaskReportAdapter;
import example.jforce.todoapplication.database.DbManager;
import example.jforce.todoapplication.model.TaskReportHelper;
import example.jforce.todoapplication.model.taskHelper;

public class Report extends AppCompatActivity {
    TextView heading, TvNoData, tv_finished, tv_pending;
    Toolbar toolbar;
    LinearLayout report_holder;
    ArrayList<TaskReportHelper> taskReportList = new ArrayList<>();
    int total_pending = 0, total_task = 0;
    RecyclerView rv_taskReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        initToolBar();
        TvNoData = findViewById(R.id.TvNoData);
        report_holder = findViewById(R.id.report_holder);
        tv_pending = findViewById(R.id.tv_pending);
        tv_finished = findViewById(R.id.tv_finished);
        rv_taskReport = findViewById(R.id.rv_taskReport);
        getReportData();
        tv_pending.setText(total_pending + "");
        tv_finished.setText((total_task - total_pending) + "/" + total_task);
        rv_taskReport.setHasFixedSize(true);
        rv_taskReport.setLayoutManager(new LinearLayoutManager(this));
        rv_taskReport.setAdapter(new TaskReportAdapter(taskReportList));
    }

    void initToolBar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        heading = findViewById(R.id.heading);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_back_black_24dp);
    }

    void getReportData() {
        ArrayList<taskHelper> taskList;
        DbManager dbm = new DbManager(Report.this);
        taskList = dbm.ListOfAllTask();
        if (taskList == null || taskList.size() == 0) {

            TvNoData.setVisibility(View.VISIBLE);
            report_holder.setVisibility(View.GONE);
        } else {
            taskReportList.clear();
            TvNoData.setVisibility(View.GONE);
            report_holder.setVisibility(View.VISIBLE);
            String selected_date = taskList.get(0).getDate();
            int pending = 0, finished = 0, total = 0;
            total_task = taskList.size();
            for (int j = 0; j < taskList.size(); j++) {
                if (taskList.get(j).getStaus().equals("0")) {
                    total_pending++;
                }
            }
            for (int i = 0; i < taskList.size(); i++) {
                if (selected_date.equalsIgnoreCase(taskList.get(i).getDate())) {
                    total++;
                    if (taskList.get(i).getStaus().equals("1")) {
                        finished++;
                    } else {
                        pending++;
                    }
                    if (i == taskList.size() - 1) {
                        TaskReportHelper trh = new TaskReportHelper(taskList.get(i).getDate(), pending, finished, total);
                        taskReportList.add(trh);
                        pending = 0;
                        finished = 0;
                        total = 0;
                    }
                } else {
                    TaskReportHelper trh = new TaskReportHelper(taskList.get(i - 1).getDate(), pending, finished, total);
                    taskReportList.add(trh);
                    pending = 0;
                    finished = 0;
                    total = 0;
                    selected_date = taskList.get(i).getDate();
                    i--;
                }
            }
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return true;
    }

}
