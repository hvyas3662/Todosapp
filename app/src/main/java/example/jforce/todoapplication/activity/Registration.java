package example.jforce.todoapplication.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import example.jforce.todoapplication.R;
import example.jforce.todoapplication.database.DbManager;
import example.jforce.todoapplication.helper.SharedPref;

public class Registration extends AppCompatActivity {
    TextView heading, registration_text;
    Toolbar toolbar;
    EditText email, name, contact, password, cnf_password;
    Button btn_reg;
    String mode = "reg";
    Switch pass_switch;
    boolean pass_switch_status = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initToolBar();
        if (getIntent() != null) {
            if (getIntent().hasExtra("mode")) {
                mode = getIntent().getStringExtra("mode");
            }
        }
        registration_text = findViewById(R.id.registration_text);
        email = findViewById(R.id.email);
        name = findViewById(R.id.name);
        contact = findViewById(R.id.contact);
        password = findViewById(R.id.password);
        cnf_password = findViewById(R.id.cnf_password);
        btn_reg = findViewById(R.id.btn_reg);
        pass_switch = findViewById(R.id.pass_switch);

        pass_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                pass_switch_status = isChecked;
                if (isChecked) {
                    password.setEnabled(true);
                    cnf_password.setEnabled(true);
                    password.setAlpha(1);
                    cnf_password.setAlpha(1);
                } else {
                    password.setEnabled(false);
                    cnf_password.setEnabled(false);
                    password.setAlpha(0.6f);
                    cnf_password.setAlpha(0.6f);
                }
            }
        });

        if (mode.equalsIgnoreCase("profile")) {
            email.setEnabled(false);
            password.setEnabled(false);
            cnf_password.setEnabled(false);
            email.setAlpha(0.6f);
            password.setAlpha(0.6f);
            cnf_password.setAlpha(0.6f);
            pass_switch.setVisibility(View.VISIBLE);
            pass_switch_status = false;

            email.setText(new SharedPref(Registration.this).getUserEmail());
            name.setText(new SharedPref(Registration.this).getUserName());
            contact.setText(new SharedPref(Registration.this).getUserContact());

        }

        if (mode.equalsIgnoreCase("profile")) {
            btn_reg.setText("Update taskList");
            registration_text.setText("Update profile");
        }
        btn_reg.setOnClickListener(new View.OnClickListener() {
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

                if (name.getText().toString().isEmpty()) {
                    name.setError("Name can not be empty");
                    name.requestFocus();
                    return;
                }

                if (contact.getText().toString().isEmpty()) {
                    contact.setError("Contact can not be empty");
                    contact.requestFocus();
                    return;
                }

                if (contact.getText().toString().length() != 10) {
                    contact.setError("Please valid contact number");
                    contact.requestFocus();
                    return;
                }

                if (pass_switch_status) {

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

                    if (cnf_password.getText().toString().isEmpty()) {
                        cnf_password.setError("Confirm Password can not be empty");
                        cnf_password.requestFocus();
                        return;
                    }


                    if (!cnf_password.getText().toString().equals(password.getText().toString())) {
                        Toast.makeText(Registration.this, "Password and Confirm Password are miss match", Toast.LENGTH_LONG).show();
                        return;
                    }

                }

                DbManager dbm = new DbManager(Registration.this);
                if (mode.equalsIgnoreCase("profile")) {
                    if (pass_switch_status) {
                        if (dbm.updateUser(name.getText().toString(), contact.getText().toString(), password.getText().toString(), new SharedPref(Registration.this).getUserId())) {
                            Toast.makeText(Registration.this, "User details updated successfully", Toast.LENGTH_LONG).show();
                            new SharedPref(Registration.this).clearAll();
                            Intent i = new Intent(Registration.this, LoginActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(Registration.this, "Some thing went wrong.", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        if (dbm.updateUser(name.getText().toString(), contact.getText().toString(), new SharedPref(Registration.this).getUserId())) {
                            Toast.makeText(Registration.this, "User details updated successfully", Toast.LENGTH_LONG).show();
                            Intent i = new Intent(Registration.this, Home.class);
                            startActivity(i);
                            finish();
                        } else {
                            Toast.makeText(Registration.this, "Some thing went wrong.", Toast.LENGTH_LONG).show();
                        }
                    }
                } else {
                    long result = dbm.insertUser(name.getText().toString(), contact.getText().toString(), email.getText().toString(), password.getText().toString());
                    if (result == -1) {
                        Toast.makeText(Registration.this, "Some thing went wrong.", Toast.LENGTH_LONG).show();
                    } else if (result == -2) {
                        Toast.makeText(Registration.this, "Email id already exist.", Toast.LENGTH_LONG).show();
                    } else if (result > 0) {
                        Toast.makeText(Registration.this, "User register successfully", Toast.LENGTH_LONG).show();
                        Intent i = new Intent(Registration.this, LoginActivity.class);
                        startActivity(i);
                        finish();
                    }
                }
            }
        });

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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }

        return true;
    }

}
