package com.example.money_saver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.ArrayList;

public class categories extends AppCompatActivity {

    SpaceNavigationView navigationView;
    public static ArrayList<String> al_cat = new ArrayList<String>();
    ListView lv_categories;
    FloatingActionButton fab;

//    ArrayAdapter<String> category_adapter;

    private ArrayList<String> categories;

    LottieAnimationView animationView;

    // Action bar UI
    ImageView iv_logout;
    TextView action_label;

    // dialog builder(pop-up window to add a category)
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText Category_name;
    private Button Add_category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        animationView = findViewById(R.id.animationView_activity);
        animationView.setAnimation("categories.json");

        categories c = new categories();

        /*al_cat.add("Food");
        al_cat.add("Medicine");
        al_cat.add("Rent");*/
        iv_logout = findViewById(R.id.logout_home);
        action_label = findViewById(R.id.activity_label);
        fab = findViewById(R.id.floatingActionButton2);
        lv_categories = findViewById(R.id.list_view);
        action_label.setText("CATEGORIES");

        // logout
        iv_logout = findViewById(R.id.logout_home);
        iv_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Logout user of firebase
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(),"Logout Successful",Toast.LENGTH_LONG).show();
                Intent intent = new Intent(categories.this,login_activity.class);
                startActivity(intent);
                finish();
            }
        });

        // Accessing ArrayList From FireBase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                if(user.Categories == null)
                {

                }
                else {
                    al_cat.clear();
                    al_cat = user.Categories;
                    System.out.println("hi hi hi: " + al_cat);
                    ArrayAdapter<String> category_adapter = new ArrayAdapter<>(categories.this, android.R.layout.simple_list_item_1, al_cat);
                    lv_categories.setAdapter(category_adapter);
                    System.out.println("ADAPTER SET: " + al_cat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("UNSUCCESSFUL");
            }
        });
        // End Of Accessing Data from FireBase


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               setAdd_category();
            }
        });

//        ArrayAdapter<String> category_adapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,al_cat);
//        lv_categories.setAdapter(category_adapter);
//        System.out.println("ADAPTER SET: "+al_cat);

        // deleting a category
        lv_categories.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                final int item_position = i;

                new AlertDialog.Builder(categories.this).setIcon(android.R.drawable.ic_delete)
                        .setTitle("Are You Sure")
                        .setMessage("Do you want to delete this item")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if(al_cat.size()>1) {
                                    al_cat.remove(item_position);
                                    // providing updated arraylist
                                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                    mDatabase.child("Users/" + FirebaseAuth.getInstance().getCurrentUser().getUid() + "/Categories").setValue(al_cat);
                                    //setting adapter
                                    ArrayAdapter<String> category_adapter = new ArrayAdapter<>(categories.this, android.R.layout.simple_list_item_1, al_cat);
                                    lv_categories.setAdapter(category_adapter);
                                    System.out.println("ADAPTER SET: " + al_cat);
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(),"Cannot delete any further categories",Toast.LENGTH_LONG).show();
                                    Toast.makeText(getApplicationContext(),"Atleast 1 category required",Toast.LENGTH_LONG).show();
                                }

                            }
                        })
                        .setNegativeButton("No",null)
                        .show();
                return true;
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
                navigationView.setCentreButtonSelectable(true);
                Intent intent = new Intent(categories.this,home_screen.class);
                startActivity(intent);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                switch(itemIndex)
                {
                    case 0:Intent intent0 = new Intent(categories.this,settings.class);
                        startActivity(intent0);
                        break;
                    case 1:Intent intent1 = new Intent(categories.this,statistics.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        break;
                    case 3:
                        Intent intent3 = new Intent(categories.this,currency_converter.class);
                        startActivity(intent3);
                        break;
                }
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                switch(itemIndex)
                {
                    case 0:Intent intent0 = new Intent(categories.this,settings.class);
                        startActivity(intent0);
                        break;
                    case 1:Intent intent1 = new Intent(categories.this,statistics.class);
                        startActivity(intent1);
                        break;
                    case 2:
                        break;
                    case 3:
                        Intent intent3 = new Intent(categories.this,currency_converter.class);
                        startActivity(intent3);
                        break;
                }
            }
        });

    }

    public void setAdd_category()
    {
        dialogBuilder = new AlertDialog.Builder(this);
        final View add_cat_view = getLayoutInflater().inflate(R.layout.popup_category,null);
        Category_name = add_cat_view.findViewById(R.id.cat_id);
        Add_category = add_cat_view.findViewById(R.id.save_btn);

        dialogBuilder.setView(add_cat_view);
        dialog = dialogBuilder.create();
        dialog.show();

        Add_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String entered_cat = Category_name.getText().toString();
                if(entered_cat.equals(""))
                {
                    Category_name.setError("Category Name Cannot Be blank");
                    Category_name.requestFocus();
                    return;
                }
                else {
                    al_cat.add(entered_cat);
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("Users/"+FirebaseAuth.getInstance().getCurrentUser().getUid()+"/Categories").setValue(al_cat);
                    // check if it already exists
                    // end of check
                }
                dialog.dismiss();
//                Intent intent = new Intent(categories.this,categories.class);
//                startActivity(intent);

            }
        });

    }
}