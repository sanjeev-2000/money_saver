package com.example.money_saver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

public class settings extends AppCompatActivity {

    SpaceNavigationView navigationView;

    public String EMAIL;

    LottieAnimationView animationView;

    //Declaring UI Elements
    TextView txt_name,txt_email,txt_phone,action_bar,txt_salary,txt_saving;
    Button btn_update;
    ImageView iv_actionbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        animationView = findViewById(R.id.animationView_activity);
        animationView.setAnimation("settings.json");

        // initializing UI elements
        txt_name = findViewById(R.id.disp_name);
        txt_email = findViewById(R.id.disp_email);
        txt_phone = findViewById(R.id.disp_phone);
        txt_salary = findViewById(R.id.disp_salary);
        txt_saving = findViewById(R.id.disp_saving);
        btn_update = findViewById(R.id.update_btn);
        action_bar = findViewById(R.id.activity_label);
        iv_actionbar = findViewById(R.id.logout_home);

        action_bar.setText("SETTINGS");

        iv_actionbar = findViewById(R.id.logout_home);
        iv_actionbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Logout user of firebase
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(),"Logout Successful",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(settings.this,login_activity.class);
                startActivity(intent);
                finish();
            }
        });

        // retrieving values from database
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                txt_name.setText(user.Name);
                txt_email.setText(user.Email);
                txt_phone.setText(user.Phone);
                txt_salary.setText(user.Salary);
                txt_saving.setText(user.Savings);
                System.out.println(user.Savings);
                EMAIL = user.Email;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("UNSUCCESSFUL");
            }
        });
        //end of retreival

        // navigate button to update details
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(settings.this,update_details.class);
                startActivity(intent);
            }
        });
        // navigation done

        navigationView = findViewById(R.id.space);
        navigationView.initWithSaveInstanceState(savedInstanceState);
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.settings_icon));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.stats_icon));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.categories_icon));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.convert_icon));


        navigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                navigationView.setCentreButtonSelectable(true);
                Intent intent = new Intent(settings.this,home_screen.class);
                startActivity(intent);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                switch(itemIndex)
                {
                    case 0:
                        break;
                    case 1:Intent intent1 = new Intent(settings.this,statistics.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(settings.this,categories.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(settings.this,currency_converter.class);
                        startActivity(intent3);
                        break;
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                switch(itemIndex)
                {
                    case 0:
                        break;
                    case 1:Intent intent1 = new Intent(settings.this,statistics.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(settings.this,categories.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        Intent intent3 = new Intent(settings.this,currency_converter.class);
                        startActivity(intent3);
                        break;
                }
            }
        });

    }
}