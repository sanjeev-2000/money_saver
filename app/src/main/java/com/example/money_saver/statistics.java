package com.example.money_saver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import java.util.ArrayList;
import java.util.List;

public class statistics extends AppCompatActivity {

    SpaceNavigationView navigationView;
    AnyChartView anyChartView;

    //Total Sum variable
    int sum=0;

    // array list to set data
    ArrayList<String> arrayList_cat = new ArrayList<String>();
    ArrayList<String> arrayList_amt_str = new ArrayList<String>();
    ArrayList<Integer> arrayList_amt = new ArrayList<Integer>();

    // To Display
    ArrayList<String> arrayListDisplay_cat = new ArrayList<String>();
    ArrayList<Integer> arrayListDisplay_amt = new ArrayList<Integer>();

    // Initializing UI elements
    TextView activity_heading;
    ImageView logout_iv;
    ProgressBar pb_load_chart;

    LottieAnimationView animationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        animationView = findViewById(R.id.animationView_activity);
        animationView.setAnimation("pie.json");

        //initializing UI elements
        pb_load_chart = findViewById(R.id.pb_pie);
        activity_heading = findViewById(R.id.activity_label);
        logout_iv = findViewById(R.id.logout_home);
        activity_heading.setText("STATISTICS");

        logout_iv = findViewById(R.id.logout_home);
        logout_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Logout user of firebase
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(),"Logout Successful",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(statistics.this,login_activity.class);
                startActivity(intent);
                finish();
            }
        });

//        pb_load_chart.setVisibility(View.VISIBLE);
//        // getting data from Firebase and setting in to_display
//        computing_values comp = new computing_values();
//        comp.setting_data();
//        comp.arrayListDisplay_cat = arrayListDisplay_cat;
//        comp.arrayListDisplay_amt = arrayListDisplay_amt;
//        System.out.println(comp.sum);
//        //setting in to_display done
//        pb_load_chart.setVisibility(View.GONE);
//
//        // displaying pie chart
//        anyChartView = findViewById(R.id.pie_canvas);
//        setup_pie();
        setting_data();


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
                Intent intent = new Intent(statistics.this,home_screen.class);
                startActivity(intent);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                switch(itemIndex)
                {
                    case 0:Intent intent0 = new Intent(statistics.this,settings.class);
                        startActivity(intent0);
                        finish();
                        break;
                    case 1:
                        break;
                    case 2:
                        Intent intent2 = new Intent(statistics.this,categories.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case 3:
                        Intent intent3 = new Intent(statistics.this,currency_converter.class);
                        startActivity(intent3);
                        finish();
                        break;
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                switch(itemIndex)
                {
                    case 0:Intent intent0 = new Intent(statistics.this,settings.class);
                        startActivity(intent0);
                        finish();
                        break;
                    case 1:
                        break;
                    case 2:
                        Intent intent2 = new Intent(statistics.this,categories.class);
                        startActivity(intent2);
                        finish();
                        break;
                    case 3:
                        Intent intent3 = new Intent(statistics.this,currency_converter.class);
                        startActivity(intent3);
                        finish();
                        break;
                }
            }
        });

    }
    public void setup_pie(ArrayList<String> d_cat,ArrayList<Integer> d_amt)
    {
        Pie pie = AnyChart.pie();
        List<DataEntry> dataEntries = new ArrayList<>();
        int i=0;
        try {
            while (i < d_amt.size()) {
//            System.out.println(arrayListDisplay_cat.get(i)+" "+arrayListDisplay_amt.get(i)+"   "+i);
                dataEntries.add(new ValueDataEntry(d_cat.get(i), d_amt.get(i)));
                i++;
            }
        }
        catch (Exception ex)
        {
            System.out.println(ex);
        }

//        System.out.println(arrayListDisplay_cat.size()+"  "+arrayListDisplay_amt.size());

        pie.data(dataEntries);
        pie.title("Expenditure");
        anyChartView.setChart(pie);
        pb_load_chart.setVisibility(View.GONE);

    }
    public void setting_data()
    {
        // getting data from Firebase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user.Respective_Cat_Amounts == null)
                {
                    Toast.makeText(getApplicationContext(),"No available data to show",Toast.LENGTH_LONG).show();
                }
                else {
                    arrayList_cat = user.Selected_Categories;
                    arrayList_amt_str = user.Respective_Cat_Amounts;
                    System.out.println("Actual Data Retreived from firebase "+user.Selected_Categories+" "+user.Respective_Cat_Amounts);
                    System.out.println("Data Retreived from firebase "+arrayList_amt_str+" "+arrayList_cat);
                    System.out.println("data retreived:"+arrayList_amt_str+" "+arrayList_cat);
                    int SUM=0;
                    //converting arraylist of string to array list of integers
                    try {

                        for (String a : arrayList_amt_str) {
                            arrayList_amt.add(Integer.parseInt(a));
                        }
                        //end of conversion
                        for (String a : arrayList_cat) {
                            if (!arrayListDisplay_cat.contains(a))
                                arrayListDisplay_cat.add(a);
                        }
                        for (String a : arrayListDisplay_cat) {
                            System.out.println(a + " ");
                            for (int i = 0; i < arrayList_cat.size(); i++) {
                                if (arrayList_cat.get(i).equals(a)) {
                                    SUM = SUM + arrayList_amt.get(i);// category amount
                                    sum = sum + arrayList_amt.get(i);// total amount
                                }
                            }
                            System.out.println(SUM);
                            arrayListDisplay_amt.add(SUM);
                            SUM = 0;
                        }
                        System.out.println("Final ArrayList" + arrayListDisplay_cat + " " + arrayListDisplay_amt);
                        System.out.println("Total amount spent:" + sum);
                        // displaying pie chart
                        anyChartView = findViewById(R.id.pie_canvas);
                        setup_pie(arrayListDisplay_cat, arrayListDisplay_amt);
                    }
                    catch (Exception e)
                    {
                        System.out.println(e);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("UNSUCCESSFUL");
            }
        });
        // end of retreival
    }
}