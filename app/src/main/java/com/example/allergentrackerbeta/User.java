package com.example.allergentrackerbeta;

public class User
{
    public String uName; // username
    public String uPass; // password
    public String uEmail; // email

    public User() // needed because there is an instance which user gets no arguments in Login button onclick
    {
        this.uName = "error";
        this.uPass = "0000";
    }
    public User(String uName, String uPass, String uEmail)
    {
        this.uName = uName;
        this.uPass = uPass;
        this.uEmail = uEmail;


    }
}
