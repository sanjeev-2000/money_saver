package com.example.money_saver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class popup_activity extends AppCompatActivity {

    EditText txtv1,txtv2;
    Button save_updates;
    private ArrayList<String> categories;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_activity);

        txtv1 = findViewById(R.id.tv1);
        txtv2 = findViewById(R.id.tv2);
        save_updates = findViewById(R.id.save_salary);

        save_updates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String SALARY = txtv1.getText().toString();
                String SAVINGS = txtv2.getText().toString();

                if(SALARY.isEmpty())
                {
                    txtv1.setError("Enter Your Salary");
                    txtv1.requestFocus();
                    return;
                }
                else if(SAVINGS.isEmpty())
                {
                    txtv2.setError("Enter Your Savings Amount");
                    txtv2.requestFocus();
                    return;
                }
                else if(!Patterns.PHONE.matcher(SALARY).matches())
                {
                    txtv1.setError("Enter a proper number");
                    txtv1.requestFocus();
                    return;
                }
                else if(!Patterns.PHONE.matcher(SAVINGS).matches())
                {
                    txtv2.setError("Enter a proper number");
                    txtv2.requestFocus();
                    return;
                }

                SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("salary", SALARY);
                editor.putString("saving", SAVINGS);
                editor.apply();

                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Salary").setValue(SALARY);
                mDatabase.child("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Savings").setValue(SAVINGS);
                Toast.makeText(getApplicationContext(),"Data Saved Successfully",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(popup_activity.this,home_screen.class);
                startActivity(intent);
            }
        });

    }
}