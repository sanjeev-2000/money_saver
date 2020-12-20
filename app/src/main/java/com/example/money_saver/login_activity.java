package com.example.money_saver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class login_activity extends AppCompatActivity {

    private String EMAIL,PASSWORD;

    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;

    LottieAnimationView log_in;

    TextView txt_register;
    EditText Email,Password;
    ProgressBar progressBar;

    // Firebase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_activity);

        log_in = findViewById(R.id.login_btn);

        ConnectivityManager cm =
                (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork == null)
        {
            no_internet();
        }
        else if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
            System.out.println("connected");
        }

        txt_register = findViewById(R.id.register);
        log_in = findViewById(R.id.login_btn);
        Email = findViewById(R.id.email_login);
        Password = findViewById(R.id.pass_login);
        progressBar = findViewById(R.id.progressBar_login);

        // firebase
        mAuth = FirebaseAuth.getInstance();

        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(login_activity.this,register_activity.class);
                startActivity(intent);
            }
        });

        log_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(login_activity.this,home_screen.class);
//                startActivity(intent);
                userLogin();
            }
        });


    }
    public void userLogin()
    {
        String email = Email.getText().toString();
        String pass = Password.getText().toString();

        // performing checks
        if(email.isEmpty())
        {
            Email.setError("Email Should not be Blank");
            Email.setText("");
            Email.requestFocus();
            return;
        }
        else if(pass.isEmpty())
        {
            Password.setError("Password Should not be Blank");
            Password.setText("");
            Password.requestFocus();
            return;
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Email.setError("Enter a valid Email id");
            Email.setText("");
            Email.requestFocus();
            return;
        }
        else if(pass.length()<8)
        {
            Password.setError("Password should be greater than 8 characters");
            Password.setText("");
            Password.requestFocus();
            return;
        }
        // checks done

        // performing login
        progressBar.setVisibility(View.VISIBLE);

        // logging user
        mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                    if(user.isEmailVerified())
                    {
                        Intent intent = new Intent(login_activity.this,home_screen.class);
                        startActivity(intent);
                        Toast.makeText(getApplicationContext(),"Login Successful!",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        progressBar.setVisibility(View.GONE);
                        user.sendEmailVerification();
                        Toast.makeText(getApplicationContext(),"Check Your Email for Verification",Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(getApplicationContext(),"Login Unsuccessful check your credentials",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
    public void no_internet(){

        dialogBuilder = new AlertDialog.Builder(this);
        final View no_internet_view = getLayoutInflater().inflate(R.layout.no_internet,null);
        TextView no_int = findViewById(R.id.tv_no_internet);
        LottieAnimationView animationView2 = findViewById(R.id.animationView2);
        dialogBuilder.setView(no_internet_view);
        dialog = dialogBuilder.create();
        dialog.show();

//        animationView.animate().translationY(-2000).setDuration(1000);
//        no_int.animate().translationY(2000).setDuration(1000);

        // check if internet is still on

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                ConnectivityManager cm =
                        (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                if(activeNetwork == null)
                {
                    System.out.println("still no internet");
                    handler.postDelayed(this,1000);
                }
                else if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI || activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                    System.out.println("connected");
                    dialog.dismiss();
                }
            }
        },2000);

    }
}