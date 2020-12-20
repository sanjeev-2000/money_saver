package com.example.money_saver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class register_activity extends AppCompatActivity {

    TextView tv_login;
    private FirebaseAuth mAuth;
    EditText Name,Email,Phone,Pass1,Pass2;
    ProgressBar pb_register;
    Button register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_activity);

        mAuth = FirebaseAuth.getInstance();

        tv_login = findViewById(R.id.goto_login);
        register_btn = findViewById(R.id.register_btn);
        Name = findViewById(R.id.name);
        Email = findViewById(R.id.email);
        Phone = findViewById(R.id.phone);
        Pass1 = findViewById(R.id.pass1);
        Pass2 = findViewById(R.id.pass2);
        pb_register = findViewById(R.id.progressBar_register);

        tv_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(register_activity.this,login_activity.class);
                startActivity(intent);
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register_user();
            }
        });


    }

    public void register_user()
    {
        final String name = Name.getText().toString();
        final String email = Email.getText().toString();
        final String phone = Phone.getText().toString();
        final String pass1 = Pass1.getText().toString();
        String pass2 = Pass2.getText().toString();

        // performing checks

        if(name.isEmpty())
        {
            Name.setError("Name is Required");
            Name.requestFocus();
            return;
        }
        else if(email.isEmpty())
        {
            Email.setError("Email is Required");
            Email.requestFocus();
            return;
        }
        else if(phone.isEmpty())
        {
            Phone.setError("Phone is Required");
            Phone.requestFocus();
            return;
        }
        else if(pass1.isEmpty())
        {
            Pass1.setError("Password is Required");
            Pass1.requestFocus();
            return;
        }
        else if(pass2.isEmpty())
        {
            Pass2.setError("Confirm Password is Required");
            Pass2.requestFocus();
            return;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Email.setError("Enter a valid Email");
            Email.requestFocus();
            return;
        }
        else if(!Patterns.PHONE.matcher(phone).matches())
        {
            Phone.setError("Enter a valid Phone Number");
            Phone.requestFocus();
            return;
        }
        else if(!pass1.equals(pass2))
        {
            Pass1.setError("Password and Confirm Ppasword Don't match");
            Pass1.setText("");
            Pass1.requestFocus();
            Pass2.setText("");
            return;
        }
        else if(pass1.length()<8)
        {
            Pass1.setError("Password should be greater than 8 characters");
            Pass1.setText("");
            Pass1.requestFocus();
            Pass2.setText("");
            return;
        }
        // finished performing checks

        // registering user in firebase
        pb_register.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email,pass1)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            User user = new User(name,phone,email,pass1);

                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                     if(task.isSuccessful())
                                     {
                                         Toast.makeText(getApplicationContext(),"Registered Successfully",Toast.LENGTH_LONG).show();
                                         pb_register.setVisibility(View.GONE);
                                         // redirect to login page
                                         Intent intent = new Intent(getApplicationContext(),login_activity.class);
                                         startActivity(intent);
                                     }
                                     else
                                     {
                                         Toast.makeText(getApplicationContext(),"Registration Not Successful",Toast.LENGTH_LONG).show();
                                         pb_register.setVisibility(View.GONE);
                                     }
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Registration Not Successful",Toast.LENGTH_LONG).show();
                            pb_register.setVisibility(View.GONE);
                        }

                    }
                });

    }
}