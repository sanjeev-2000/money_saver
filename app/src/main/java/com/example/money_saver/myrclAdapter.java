package com.example.money_saver;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class myrclAdapter extends RecyclerView.Adapter<myrclAdapter.holder> {

    String[] cat,amt;
    ArrayList<String> al_cat,al_amt;

    public myrclAdapter() {

        // retreiving from database here
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
                    al_cat = user.Selected_Categories;
                    al_amt = user.Respective_Cat_Amounts;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("UNSUCCESSFUL");
            }
        });
        // end of retreival

        this.al_cat = al_cat;
        this.al_amt = al_amt;
    }

    @NonNull
    @Override
    public holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.recycler_view_items,parent,false);
        return new holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull holder holder, int position) {

        holder.category.setText(al_cat.get(position));
        holder.amount.setText(al_amt.get(position));

    }

    @Override
    public int getItemCount() {
        if(al_cat == null)
            return 0;
        else
            return al_cat.size();
    }


    class holder extends RecyclerView.ViewHolder
    {
        TextView category,amount;
        public holder(@NonNull View itemView) {
            super(itemView);
            category = itemView.findViewById(R.id.category_display);
            amount = itemView.findViewById(R.id.amount_display);

        }
    }
}
