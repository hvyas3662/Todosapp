package example.jforce.todoapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import example.jforce.todoapplication.R;
import example.jforce.todoapplication.database.DbManager;
import example.jforce.todoapplication.helper.SharedPref;

public class LoginActivity extends AppCompatActivity {
    TextView tv_rge, heading;
    Toolbar toolbar;
    EditText email, password;
    Button btn_login;
    LinearLayout holder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initToolBar();
        holder = findViewById(R.id.holder);
        DbManager dbm = new DbManager(LoginActivity.this);
        if (dbm.LoginUser(new SharedPref(this).getsp().getString("email", ""), new SharedPref(this).getsp().getString("pass", ""))) {
            Intent i = new Intent(LoginActivity.this, Home.class);
            startActivity(i);
            finish();
        } else {
            holder.setVisibility(View.VISIBLE);
        }
        tv_rge = findViewById(R.id.tv_rge);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().isEmpty()) {
                    email.setError("Email can not be empty");
                    email.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(email.getText().toString()).matches()) {
                    email.setError("Please enter valid email address");
                    email.requestFocus();
                    return;
                }

                if (password.getText().toString().isEmpty()) {
                    password.setError("Password can not be empty");
                    password.requestFocus();
                    return;
                }
                if (password.getText().toString().length() <= 5) {
                    password.setError("Password length Should be grater the 5");
                    password.requestFocus();
                    return;
                }
                DbManager dbm = new DbManager(LoginActivity.this);
                if (dbm.LoginUser(email.getText().toString(), password.getText().toString())) {
                    Intent i = new Intent(LoginActivity.this, Home.class);
                    startActivity(i);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Email or password are wrong.", Toast.LENGTH_LONG).show();
                }
            }

        });

        tv_rge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, Registration.class);
                startActivity(i);
            }
        });
    }


    void initToolBar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        heading = findViewById(R.id.heading);
        setSupportActionBar(toolbar);
    }
}
