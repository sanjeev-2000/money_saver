package com.example.money_saver;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class User {

    public String Name,Phone,Email,Password,Salary,Savings;
    public ArrayList<String> Categories,Selected_Categories,Respective_Cat_Amounts;

    public User() // just to access data values
    {

    }

    public User(String Name,String Phone,String Email,String Password)
    {
        this.Name = Name;
        this.Email = Email;
        this.Phone = Phone;
        this.Password = Password;
    }

    // Saving Salary and To_Save Amount Details
    public User(String Name,String Phone,String Email,String Password,String Salary,String Savings,ArrayList<String> Categories,ArrayList<String> Selected_Categories,ArrayList<String> Respective_Cat_Amounts)
    {
        this.Name = Name;
        this.Email = Email;
        this.Phone = Phone;
        this.Password = Password;
        this.Salary = Salary;
        this.Savings = Savings;
        this.Categories = Categories;
        this.Selected_Categories = Selected_Categories;
        this.Respective_Cat_Amounts = Respective_Cat_Amounts;
    }


}
