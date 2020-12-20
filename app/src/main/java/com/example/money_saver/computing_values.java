package com.example.money_saver;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class computing_values {
    //Total Sum variable
    public int sum=0;

    // array list to set data
    ArrayList<String> arrayList_cat = new ArrayList<String>();
    ArrayList<String> arrayList_amt_str = new ArrayList<String>();
    ArrayList<Integer> arrayList_amt = new ArrayList<Integer>();

    // To Display
    ArrayList<String> arrayListDisplay_cat = new ArrayList<String>();
    ArrayList<Integer> arrayListDisplay_amt = new ArrayList<Integer>();

    public computing_values(ArrayList<String> arrayList_cat,ArrayList<String> arrayList_amt_str) {
        this.arrayList_cat = arrayList_cat;
        this.arrayList_amt_str = arrayList_amt_str;
    }

    public void setting_data()
    {
        System.out.println("data retreived:"+arrayList_amt_str+" "+arrayList_cat);
        int SUM=0;
        //converting arraylist of string to array list of integers
        for(String a:arrayList_amt_str)
        {
            arrayList_amt.add(Integer.parseInt(a));
        }
        //end of conversion
        for(String a:arrayList_cat)
        {
            if(!arrayListDisplay_cat.contains(a))
                arrayListDisplay_cat.add(a);
        }
        for(String a:arrayListDisplay_cat)
        {
            for(int i=0;i<=arrayList_cat.size();i++)
            {
                if(i<arrayList_amt.size())
                if(arrayList_cat.get(i).equals(a))
                {
                    SUM = SUM + arrayList_amt.get(i);// category amount
                    sum = sum + arrayList_amt.get(i);// total amount
                }
            }
            arrayListDisplay_amt.add(SUM);
            SUM=0;
        }
        System.out.println("Final ArrayList"+arrayListDisplay_cat+" "+arrayListDisplay_amt);
        System.out.println("Total amount spent:"+sum);
    }
}
