package com.example.money_saver;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.content.SharedPreferences.Editor;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.zip.Inflater;

public class home_screen extends AppCompatActivity {

    // checking if build version is greater than or equal to oreo

    private static int INCOME=0;
    private static int TO_SAVE=0;
    private ProgressBar progressBarAnimation;
    private FloatingActionButton fab_home;
    private ObjectAnimator progressAnimator;

    private int percentage_used;

    // lv
    ListView listView;

    //textView Salary and Savings
    TextView tv_sal,tv_sav;

    // bottom navigation
    SpaceNavigationView navigationView;

    // Tool Bar Elements
    ImageView iv_logout;

//  Popup Activity(Add Transaction)
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText amount;
    private Button Add_transaction;
    Spinner cat_spinner;
    String selected_category="";
    public ArrayList<String> al_category = new ArrayList<String>();
    public ArrayList<String> al_amount  = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

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
            //creating Notification channel for devices greater than O
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel("custom_notif", "Limit Reached", NotificationManager.IMPORTANCE_DEFAULT);
                NotificationManager manager = getSystemService(NotificationManager.class);
                manager.createNotificationChannel(channel);
            }

            //List view initialization
            listView = findViewById(R.id.rclview);

            tv_sal = findViewById(R.id.disp1);
            tv_sav = findViewById(R.id.disp2);

            // logout imageView
            iv_logout = findViewById(R.id.logout_home);
            iv_logout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Logout user of firebase
                    FirebaseAuth.getInstance().signOut();
                    Toast.makeText(getApplicationContext(), "Logout Successful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(home_screen.this, login_activity.class);
                    startActivity(intent);
                    finish();
                }
            });

            // fab
            fab_home = findViewById(R.id.floatingActionButton_home);
            fab_home.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    add_transaction();
                }
            });

            // saving data
            SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
            Editor editor = pref.edit();
            SharedPreferences mypref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
            boolean firstStart = pref.getBoolean("pref", true);


            // add sample data
//        al_category.add("fuel");al_category.add("food");al_category.add("grocery");
//        al_amount.add("220");al_amount.add("456");al_amount.add("1298");

            // retreiving data from Firebase to get transactions.
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    User user = dataSnapshot.getValue(User.class);
//                tv_sal.setText(user.Salary);
//                tv_sav.setText(user.Savings);
                    if (user.Respective_Cat_Amounts == null && user.Selected_Categories == null) {
                        tv_sav.setText("0");
                    } else {
                        al_category = user.Selected_Categories;
                        al_amount = user.Respective_Cat_Amounts;
                        System.out.println(al_category + " " + al_amount);
                        //calculating total amount spend
                        int sum = 0;
                        for (String num : al_amount) {
                            sum = sum + Integer.parseInt(num);
                        }
                        tv_sav.setText(sum + "");
                        if (sum > (Integer.parseInt(user.Salary) - Integer.parseInt(user.Savings))) {
                            percentage_used = 100;
                            Toast.makeText(getApplicationContext(), "You have used all of your salary ,now to save spend less", Toast.LENGTH_LONG).show();
                            NotificationCompat.Builder mbuilder = new NotificationCompat.Builder(home_screen.this, "custom_notif");
                            mbuilder.setContentTitle("Limit Reached");
                            mbuilder.setContentText("You have Reached your savings target, cut down on your cost to save");
                            mbuilder.setSmallIcon(R.drawable.empty_wallet);
                            mbuilder.setAutoCancel(true);

                            NotificationManagerCompat managerCompat = NotificationManagerCompat.from(home_screen.this);
                            managerCompat.notify(1, mbuilder.build());
                            System.out.println("Notification sent!!!");

                        } else {
                            percentage_used = (sum * 100) / Integer.parseInt(user.Salary);
                            System.out.println("p:" + percentage_used + " " + sum);
                        }
                        // setting list view adapter
                        init();
                        progressAnimator.setDuration(2000);
                        progressAnimator.start();
                        progressAnimator.addListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationEnd(Animator animation) {
                                super.onAnimationEnd(animation);
//                Toast.makeText(getApplicationContext(),"hi",Toast.LENGTH_SHORT).show();
                            }
                        });
                        myCustomAdapter mca = new myCustomAdapter(home_screen.this, al_category, al_amount);
                        listView.setAdapter(mca);
                        System.out.println("List View Adapter Set");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    System.out.println("UNSUCCESSFUL");
                }
            });
            // End Of Accessing Data from FireBase


            // check if its first time login
            if (firstStart) {
                Intent intent = new Intent(home_screen.this, popup_activity.class);
                startActivity(intent);
                editor.putBoolean("pref", false);
                editor.apply();
            }

            // deleting a transaction
            listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                    final int item_position = i;

                    new AlertDialog.Builder(home_screen.this).setIcon(android.R.drawable.ic_delete)
                            .setTitle("Are You Sure")
                            .setMessage("Do you want to delete this item")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    al_category.remove(item_position);
                                    al_amount.remove(item_position);
                                    // providing updated arraylist
                                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                    mDatabase.child("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/Selected_Categories").setValue(al_category);
                                    mDatabase.child("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/Respective_Cat_Amounts").setValue(al_amount);
                                    //setting adapter
//                                ArrayAdapter<String> category_adapter = new ArrayAdapter<>(categories.this, android.R.layout.simple_list_item_1, al_cat);
//                                lv_categories.setAdapter(category_adapter);
//                                System.out.println("ADAPTER SET: " + al_cat);
                                    myCustomAdapter mca = new myCustomAdapter(home_screen.this, al_category, al_amount);
                                    listView.setAdapter(mca);
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                    return true;
                }
            });

            // Bottom navigation
            navigationView = findViewById(R.id.space);
            navigationView.initWithSaveInstanceState(savedInstanceState);
            navigationView.addSpaceItem(new SpaceItem("", R.drawable.settings_icon));
            navigationView.addSpaceItem(new SpaceItem("", R.drawable.stats_icon));
            navigationView.addSpaceItem(new SpaceItem("", R.drawable.categories_icon));
            navigationView.addSpaceItem(new SpaceItem("", R.drawable.convert_icon));

            navigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
                @Override
                public void onCentreButtonClick() {
                }

                @Override
                public void onItemClick(int itemIndex, String itemName) {
                    switch (itemIndex) {
                        case 0:
                            Intent intent0 = new Intent(home_screen.this, settings.class);
                            startActivity(intent0);
                            break;
                        case 1:
                            Intent intent1 = new Intent(home_screen.this, statistics.class);
                            startActivity(intent1);
                            break;
                        case 2:
                            Intent intent2 = new Intent(home_screen.this, categories.class);
                            startActivity(intent2);
                            break;
                        case 3:
                            Intent intent3 = new Intent(home_screen.this, currency_converter.class);
                            startActivity(intent3);
                            break;
                    }
                }

                @Override
                public void onItemReselected(int itemIndex, String itemName) {
                    switch (itemIndex) {
                        case 0:
                            Intent intent0 = new Intent(home_screen.this, settings.class);
                            startActivity(intent0);
                            break;
                        case 1:
                            Intent intent1 = new Intent(home_screen.this, statistics.class);
                            startActivity(intent1);
                            break;
                        case 2:
                            Intent intent2 = new Intent(home_screen.this, categories.class);
                            startActivity(intent2);
                            break;
                        case 3:
                            Intent intent3 = new Intent(home_screen.this, currency_converter.class);
                            startActivity(intent3);
                            break;
                    }
                }
            });
        }
    // Bottom navigation ends

    // progress bar setting limit
    private void init()
    {
        progressBarAnimation = findViewById(R.id.progressBar);
        progressAnimator = ObjectAnimator.ofInt(progressBarAnimation,"progress",0,percentage_used);
    }

    // popup activity to add expense
    public void add_transaction()
    {

        categories c = new categories();
        dialogBuilder = new AlertDialog.Builder(this);
        final View add_transaction_view = getLayoutInflater().inflate(R.layout.popup_home,null);
        cat_spinner = add_transaction_view.findViewById(R.id.category_spinner); // spinner
        Add_transaction = add_transaction_view.findViewById(R.id.add_expense); // button
        amount = add_transaction_view.findViewById(R.id.et_amount); // editText

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,c.al_cat);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        cat_spinner.setAdapter(arrayAdapter);


        dialogBuilder.setView(add_transaction_view);
        dialog = dialogBuilder.create();
        dialog.show();

        cat_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selected_category = adapterView.getItemAtPosition(i).toString();
//                Toast.makeText(getApplicationContext(),selected_category+" selected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        Add_transaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String entered_amount = amount.getText().toString();

                if(entered_amount.isEmpty())
                {
                    amount.setError("Amount cannot be Blank");
                    amount.requestFocus();
                    return;
                }
                else if(!Pattern.compile("[0-9]+").matcher(entered_amount).matches())
                {
                    amount.setError("Amount should be a Number");
                    amount.requestFocus();
                    return;
                }

                System.out.println(selected_category);
                al_category.add(selected_category);
                al_amount.add(entered_amount);
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Selected_Categories").setValue(al_category);
                mDatabase.child("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Respective_Cat_Amounts").setValue(al_amount);

                dialog.dismiss();
//                Intent intent = new Intent(home_screen.this,home_screen.class);
//                startActivity(intent);
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

}// end of home_screen class

class myCustomAdapter extends ArrayAdapter<String>
{
    private final Activity context;
    ArrayList<String> catg,amt;

    myCustomAdapter(Activity c,ArrayList<String> catg,ArrayList<String> amt)
    {
        super(c, R.layout.recycler_view_items,catg);
        this.context = c;
        this.amt = amt;
        this.catg = catg;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = context.getLayoutInflater();
        View row = layoutInflater.inflate(R.layout.recycler_view_items,null,true);

        // refrencing from layout
        TextView tv_cat = row.findViewById(R.id.category_display);
        TextView tv_amt = row.findViewById(R.id.amount_display);

        home_screen hs = new home_screen();
        System.out.println(catg+" "+amt);
        tv_cat.setText(catg.get(position));
        tv_amt.setText(amt.get(position));

//        return super.getView(position, convertView, parent);
        return row;
    }
}



