package com.example.money_saver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class update_details extends AppCompatActivity {

    // Declaring UI elements
    TextView reset_pass;
    EditText edt_name,edt_email,edt_phone,edt_salary,edt_saving;
    Button btn_back,btn_save;
    ProgressBar pb_update;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details);

        //Initializing UI elements
        reset_pass = findViewById(R.id.reset_password);
        edt_name = findViewById(R.id.et_name);
        edt_email = findViewById(R.id.et_email);
        edt_phone = findViewById(R.id.et_phone);
        edt_salary = findViewById(R.id.et_sal);
        edt_saving = findViewById(R.id.et_save);
        pb_update = findViewById(R.id.pb_update);
        btn_save = findViewById(R.id.save_btn);
        btn_back = findViewById(R.id.back_btn);

        //Retreiving data From FireBase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                edt_name.setText(user.Name);
                edt_email.setText(user.Email);
                edt_phone.setText(user.Phone);
                edt_salary.setText(user.Salary);
                edt_saving.setText(user.Savings);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("UNSUCCESSFUL");
            }
        });
        // End Of Accessing Data from FireBase

        // adding listener to back Button
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(update_details.this,settings.class);
                startActivity(intent);
            }
        });

        // adding listener to reset password
        reset_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(update_details.this,reset_password.class);
                startActivity(intent);
            }
        });

        // adding on click Listener to Save Button
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name = edt_name.getText().toString();
                String Email = edt_email.getText().toString();
                String Phone = edt_phone.getText().toString();
                String Salary = edt_salary.getText().toString();
                String Savings = edt_saving.getText().toString();
                // checks to see if everything is proper
                if(Name.isEmpty())
                {
                    edt_name.setError("Name is Required");
                    edt_name.requestFocus();
                    return;
                }
                else if(Email.isEmpty())
                {
                    edt_email.setError("Email is Required");
                    edt_email.requestFocus();
                    return;
                }
                else if(Phone.isEmpty())
                {
                    edt_phone.setError("Phone is Required");
                    edt_phone.requestFocus();
                    return;
                }
                else if(Salary.isEmpty())
                {
                    edt_phone.setError("Phone is Required");
                    edt_phone.requestFocus();
                    return;
                }
                else if(Savings.isEmpty())
                {
                    edt_phone.setError("Phone is Required");
                    edt_phone.requestFocus();
                    return;
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(Email).matches())
                {
                    edt_email.setError("Enter a valid Email");
                    edt_email.requestFocus();
                    return;
                }
                else if(!Patterns.PHONE.matcher(Phone).matches())
                {
                    edt_phone.setError("Enter a valid Phone Number");
                    edt_phone.requestFocus();
                    return;
                }
                // end of checks

                // updating data in firebase
                pb_update.setVisibility(View.VISIBLE);
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Salary").setValue(Salary);
                mDatabase.child("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Savings").setValue(Savings);
                mDatabase.child("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Email").setValue(Email);
                mDatabase.child("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Name").setValue(Name);
                mDatabase.child("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Phone").setValue(Phone);
                // firebase data updated
                pb_update.setVisibility(View.GONE);
                Toast.makeText(update_details.this,"Database Updated Successfully",Toast.LENGTH_LONG).show();

                Intent intent = new Intent(update_details.this,settings.class);
                startActivity(intent);
            }
        });

    }
}