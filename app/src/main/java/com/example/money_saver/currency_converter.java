package com.example.money_saver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

public class currency_converter extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    SpaceNavigationView navigationView;

    // action bar UI elements
    ImageView iv_logout;
    TextView action_label;

    LottieAnimationView animationView;

    // currency converter
    Button btn_convert ;
    public static TextView data;
    Spinner spinner1;
    Spinner spinner2;
    public static EditText amt;
    public static Double amount;
    public static String c1,c2;
    String[] Country_codes = {"EURO","US DOLLER","POUND STERLING","INDIAN RUPEE","AUSTRALIAN DOLLER","CANADIAN DOLLER","HONG KONG DOLLER","PHILIPPINE PESO","YUAN RENMINBI","YEN","MEXICAN PESO"};
    String[] Countries1 = {"EUR","USD","GBP","INR","AUD","CAD","HKD","PHP","CNY","JPY","MXN"};
    String[] Countries2 = {"EUR","USD","GBP","INR","AUD","CAD","HKD","PHP","CNY","JPY","MXN"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency_converter);

        animationView = findViewById(R.id.animationView_activity);
        animationView.setAnimation("converter.json");

        iv_logout = findViewById(R.id.logout_home);
        action_label = findViewById(R.id.activity_label);
        action_label.setText("CONVERTER");

        // logout
        iv_logout = findViewById(R.id.logout_home);
        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Logout user of firebase
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(),"Logout Successful",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(currency_converter.this,login_activity.class);
                startActivity(intent);
                finish();
            }
        });

        navigationView = findViewById(R.id.space);
        navigationView.initWithSaveInstanceState(savedInstanceState);
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.settings_icon));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.stats_icon));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.categories_icon));
        navigationView.addSpaceItem(new SpaceItem("", R.drawable.convert_icon));

        navigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                Toast.makeText(currency_converter.this,"onCentreButtonClick", Toast.LENGTH_SHORT).show();
                navigationView.setCentreButtonSelectable(true);
                Intent intent = new Intent(currency_converter.this,home_screen.class);
                startActivity(intent);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                Toast.makeText(currency_converter.this, itemIndex + " " + itemName, Toast.LENGTH_SHORT).show();
                switch(itemIndex)
                {
                    case 0:Intent intent0 = new Intent(currency_converter.this,settings.class);
                        startActivity(intent0);
                        break;
                    case 1:Intent intent1 = new Intent(currency_converter.this,statistics.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(currency_converter.this,categories.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        break;
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                switch(itemIndex)
                {
                    case 0:Intent intent0 = new Intent(currency_converter.this,settings.class);
                        startActivity(intent0);
                        break;
                    case 1:Intent intent1 = new Intent(currency_converter.this,statistics.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        Intent intent2 = new Intent(currency_converter.this,categories.class);
                        startActivity(intent2);
                        break;
                    case 3:
                        break;
                }
            }
        });

        data = (TextView) findViewById(R.id.tv_answer);
        btn_convert = (Button) findViewById(R.id.convert);
        spinner1 = findViewById(R.id.spinner1);
        spinner2 = findViewById(R.id.spinner2);
        amt = findViewById(R.id.editText_amt);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, Country_codes);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(arrayAdapter);
        spinner1.setOnItemSelectedListener(this);

        // second spinner:
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, Country_codes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(adapter);
        spinner2.setOnItemSelectedListener(this);

        btn_convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(amt.getText().toString().isEmpty())
                {
                    amt.setError("Amount cannot be blank");
                    amt.requestFocus();
                    return;
                }
                else if(amt.getText().toString() != "") {
                    amount = Double.parseDouble(amt.getText().toString());
                    Log.d("STATE", "  " + amount);
                    fetchData fd = new fetchData();
                    fd.execute();
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please Enter Valid Amount!!!",
                            Toast.LENGTH_SHORT);
                }
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        Spinner spin = (Spinner)adapterView;
        Spinner spin2 = (Spinner)adapterView;
        if(spin.getId() == R.id.spinner1)
        {
            Toast.makeText(this, "You choose :" + Countries1[i],Toast.LENGTH_SHORT).show();
            c1 = Countries1[i];
        }
        if(spin2.getId() == R.id.spinner2)
        {
            Toast.makeText(this, "You choose :" + Countries2[i],Toast.LENGTH_SHORT).show();
            c2 = Countries2[i];
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        Toast.makeText(adapterView.getContext(),"Choose a valid country",Toast.LENGTH_SHORT).show();
    }

}