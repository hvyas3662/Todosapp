package example.jforce.todoapplication.activity;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import example.jforce.todoapplication.R;
import example.jforce.todoapplication.adapters.RvTaskAdapter;
import example.jforce.todoapplication.database.DbManager;
import example.jforce.todoapplication.helper.EqualSpacingItemDecoration;
import example.jforce.todoapplication.helper.SharedPref;
import example.jforce.todoapplication.interfaces.finishClickListener;
import example.jforce.todoapplication.model.taskHelper;

public class Home extends AppCompatActivity {
    TextView heading, TvNoData;
    Toolbar toolbar;
    FloatingActionButton add_task;
    int day, month, year;
    TabLayout tabLayout;
    DrawerLayout drawer;
    NavigationView nav;
    RecyclerView rv_task;
    ArrayList<taskHelper> taskList;
    String mode = "Todos";
    finishClickListener listener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initToolBar();
        rv_task = (RecyclerView) findViewById(R.id.rv_task);
        drawer = (DrawerLayout) findViewById(R.id.drawer);
        nav = (NavigationView) findViewById(R.id.nav);
        add_task = (FloatingActionButton) findViewById(R.id.add_task);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        TvNoData = findViewById(R.id.TvNoData);

        //nav item click events
        nav.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.report) {
                    Intent i = new Intent(Home.this, Report.class);
                    startActivity(i);
                } else if (id == R.id.profile) {
                    Intent i = new Intent(Home.this, Registration.class);
                    i.putExtra("mode", "profile");
                    startActivity(i);
                } else if (id == R.id.logout) {
                    new SharedPref(Home.this).clearAll();
                    Intent i = new Intent(Home.this, LoginActivity.class);
                    startActivity(i);
                    finish();
                }
                return true;
            }
        });

        tabLayout.addTab(tabLayout.newTab().setText("Todos"));
        tabLayout.addTab(tabLayout.newTab().setText("Finished"));


        // call when task's finish btn clicked
        listener = new finishClickListener() {
            @Override
            public void finishClick(String id) {
                DbManager dbm = new DbManager(Home.this);
                if (dbm.UpdateTask(id)) {
                    Toast.makeText(Home.this, "Task status changed successfully", Toast.LENGTH_LONG).show();
                    reload_list();
                } else {
                    Toast.makeText(Home.this, "Some thing went wrong", Toast.LENGTH_LONG).show();
                }
            }
        };

        //on bottom tabBar tab change
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mode = tab.getText().toString();
                reload_list();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // getting current date
        Calendar c = Calendar.getInstance();
        day = c.get(Calendar.DAY_OF_MONTH);
        month = (c.get(Calendar.MONTH));
        year = c.get(Calendar.YEAR);

        rv_task.setHasFixedSize(true);
        rv_task.setLayoutManager(new LinearLayoutManager(this));
        rv_task.addItemDecoration(new EqualSpacingItemDecoration(20, 1));
        reload_list();

        // on add new task
        add_task.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View dialog_view = getLayoutInflater().inflate(R.layout.dialog_view, null, false);
                final AlertDialog dialog = new AlertDialog.Builder(Home.this).setView(dialog_view).show();
                final EditText task_data = dialog_view.findViewById(R.id.task_data);
                TextView tvDate = dialog_view.findViewById(R.id.tvDate);
                tvDate.setText("Date : " + formattedDate(year + "-" + month + "-" + day));
                Button submit = dialog_view.findViewById(R.id.submit);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DbManager dbm = new DbManager(Home.this);
                        if (task_data.getText().toString().isEmpty()) {
                            Toast.makeText(Home.this, "Task a cannot be empty", Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (dbm.insertTask((year + "-" + month + "-" + day), task_data.getText().toString())) {
                            reload_list();
                        } else {
                            Toast.makeText(Home.this, "No taskList found", Toast.LENGTH_LONG).show();
                        }

                        dialog.dismiss();
                    }
                });

            }
        });
    }

    void initToolBar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        heading = (TextView) findViewById(R.id.heading);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);
    }

    void reload_list() {
        DbManager dbm = new DbManager(Home.this);
        if (mode.equalsIgnoreCase("Todos")) {
            taskList = dbm.ListOfTask(year + "-" + month + "-" + day, "0");
        } else {
            taskList = dbm.ListOfTask(year + "-" + month + "-" + day, "1");
        }
        if (taskList != null && taskList.size() > 0) {
            TvNoData.setVisibility(View.GONE);
            rv_task.setVisibility(View.VISIBLE);
            rv_task.setAdapter(new RvTaskAdapter(taskList, listener));
        } else {
            rv_task.setVisibility(View.GONE);
            TvNoData.setVisibility(View.VISIBLE);
            if (mode.equalsIgnoreCase("Todos")) {
                TvNoData.setText("No task found for " + formattedDate(year + "-" + month + "-" + day));
            } else {
                TvNoData.setText("No finished task found for " + formattedDate(year + "-" + month + "-" + day));
            }
        }
    }

    String formattedDate(String dateString) {
        String final_date = "";
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = fmt.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat fmtOut = new SimpleDateFormat("EEE, dd MMM yyyy");
        final_date = fmtOut.format(date);
        return final_date;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.cal) {
            DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    Home.this.year = year;
                    Home.this.month = month;
                    Home.this.day = dayOfMonth;
                    reload_list();
                }
            };
            new DatePickerDialog(Home.this, myDateListener, year, month, day).show();
        }

        if (item.getItemId() == android.R.id.home) {
            drawer.openDrawer(Gravity.START);
        }

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (new SharedPref(this).getUserId().equals("-1")) {
            Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
            return;
        }
        nav.getMenu().getItem(0).setChecked(true);
    }


}
