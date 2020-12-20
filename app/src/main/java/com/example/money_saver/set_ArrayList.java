package com.example.money_saver;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class set_ArrayList {

    ArrayList<String> al_ctg = new ArrayList<String>();

    public static void main(String args[])
    {
        System.out.println("hi hi hi 234");
    }
    public ArrayList<String> set_arraylist()
    {
        // Accessing ArrayList From FireBase
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("Users/"+ FirebaseAuth.getInstance().getCurrentUser().getUid());
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                al_ctg = user.Categories;
                System.out.println("DONE: "+al_ctg);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("UNSUCCESSFUL");
            }
        });
        // End Of Accessing Data from FireBase
        return al_ctg;
    }

}
