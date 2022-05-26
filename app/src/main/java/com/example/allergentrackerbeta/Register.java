package com.example.allergentrackerbeta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;

public class Register extends AppCompatActivity {
    // views
    Button registerBtn;
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /*SharedPreferences sp = getSharedPreferences("com.example.allergentrackerbeta", 0 );
        SharedPreferences.Editor sedt = sp.edit ();*/


        registerBtn = findViewById(R.id.confirmBtn);
        registerBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // user credentials to strings
                EditText username = findViewById(R.id.userName);
                String string_username = username.getText().toString();

                EditText password = findViewById(R.id.password);
                String string_password = password.getText().toString();
                //EditText phonenumber = findViewById(R.id.phoneNum);
                //String string_phonenumber = phonenumber.getText().toString();
                // if the user credentials are legal
                if(validateUser(string_username, string_password))
                {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference users = database.getReference("Users").child(string_username);
                    users.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            if (dataSnapshot.exists()) // username exists
                                Toast.makeText(getApplicationContext(), "שם משתמש תפוס", Toast.LENGTH_SHORT).show();
                            else //username doesn't exist
                            {
                                User usr = new User(string_username, string_password);
                                DatabaseReference userToAdd = database.getReference("Users").child(usr.uName);
                                userToAdd.setValue(usr);

                                /*// User object into json and save in shared prefrences
                                Gson gson = new Gson();
                                String json = gson.toJson(usr);
                                sedt.putString("User",json);
                                sedt.commit();*/

                                //String msg = "Thank you for registering to Allergen Tracker! \nYour username is " + string_username + " and password is " + string_password;
                                //sendSMS(string_phonenumber, msg);

                                Toast.makeText(getApplicationContext(), "המשתמש " + usr.uName + " נוצר בהצלחה", Toast.LENGTH_SHORT).show();

                                finish();
                                }
                        }
                        @Override
                        public void onCancelled(DatabaseError error)
                        {
                            // Failed to read value
                            Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else // invalid user
                {
                    Toast.makeText(getApplicationContext(), "invalid user credentials", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back = findViewById(R.id.backBtn2);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }

    public boolean validateUser(String username, String password)
    {
        //check if user exists
        if(checkUsername(username) && checkPassword(password))
            return true;
        else
            return false;
    }


    public boolean checkUsername(String p)
    {
        if(!p.matches("^[a-zA-Z0-9._-]{3,}$"))
            return false;
        else
            return true;
    }

    public boolean checkPassword(String p)
    {
        int length = p.length();
        if(length < 5)
            return false;
        else
            return true;
    }

    // phone has to be 10 digits
    //public boolean checkPhone(String p)
    //{
    //    if(!p.matches("[0][5][0-9]{8}"))
    //        return false;
    //    else
    //        return true;
    //}

    //public void sendSMS(String num,String msg)
    //{
    //    try
    //    {
    //        SmsManager smsManager = SmsManager.getDefault();
    //        smsManager.sendTextMessage(num, null, msg, null, null);
    //    }
    //    catch (Exception e)
    //    {
    //        Toast.makeText(getApplicationContext(), "unable to send SMS message", Toast.LENGTH_SHORT).show();
    //        e.printStackTrace();
    //    }
    //}
}