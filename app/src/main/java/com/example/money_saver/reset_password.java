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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class reset_password extends AppCompatActivity {

    // Declaring UI elements
    Button btn_update_pass;
    EditText et_update_pass;
    ProgressBar pb_update_pass;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        //creating settings object to access email

        // Initializing UI elements
        btn_update_pass = findViewById(R.id.update_pass_btn);
        et_update_pass = findViewById(R.id.update_email);
        pb_update_pass = findViewById(R.id.pb_update_pass);

        auth = FirebaseAuth.getInstance();
        btn_update_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reset_password();
            }
        });

    }
    public void reset_password(){
        // performing checks on entered email
        String email = et_update_pass.getText().toString().trim();
        if(email.isEmpty())
        {
            et_update_pass.setError("Email Should not be empty");
            et_update_pass.requestFocus();
            return;
        }
//         checks done

        pb_update_pass.setVisibility(View.VISIBLE);
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(reset_password.this,"Check Your Email to update Password",Toast.LENGTH_LONG);
                    pb_update_pass.setVisibility(View.GONE);
                    Intent intent = new Intent(reset_password.this,settings.class);
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(reset_password.this,"Password Reset Unsuccessful",Toast.LENGTH_LONG);
                    pb_update_pass.setVisibility(View.GONE);
                }
            }
        });

    }
}