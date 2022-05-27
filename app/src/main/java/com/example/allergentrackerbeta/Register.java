package com.example.allergentrackerbeta;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
//TODO: need to work out on how to send email verification. Users will need to be stored in firebaseAuth database instead? might change login process as well
public class Register extends AppCompatActivity {
    // views
    Button registerBtn;
    Button back;
    EditText username;
    EditText email;
    EditText password;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        /*SharedPreferences sp = getSharedPreferences("com.example.allergentrackerbeta", 0 );
        SharedPreferences.Editor sedt = sp.edit ();*/


        registerBtn = findViewById(R.id.confirmBtn);
        username = (EditText) findViewById(R.id.userName);
        email = (EditText) findViewById(R.id.eMail);
        password = (EditText) findViewById(R.id.password);
        fAuth = FirebaseAuth.getInstance();
        registerBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                // user credentials to strings

                String string_username = username.getText().toString();
                String string_email = email.getText().toString();
                String string_password = password.getText().toString();
                //EditText phonenumber = findViewById(R.id.phoneNum);
                //String string_phonenumber = phonenumber.getText().toString();
                // if the user credentials are legal
                if(validateUser(string_username, string_password, string_email))
                {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference users = database.getReference("Users").child(string_username);
                    users.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot)
                        {
                            if (dataSnapshot.child(string_username).exists()) // username  exists
                                Toast.makeText(getApplicationContext(), "שם משתמש תפוס", Toast.LENGTH_SHORT).show();
                            if( dataSnapshot.child(string_email).exists())
                                Toast.makeText(getApplicationContext(), "כתובת אימייל תפוסה", Toast.LENGTH_SHORT).show(); //email exists
                            else //username doesn't exist
                            {
                                User usr = new User(string_username, string_password, string_email);
                                DatabaseReference userToAdd = database.getReference("Users").child(usr.uName);
                                //sendEmailVerification( fAuth );
                                userToAdd.setValue(usr);

                                /*// User object into json and save in shared prefrences
                                Gson gson = new Gson();
                                String json = gson.toJson(usr);
                                sedt.putString("User",json);
                                sedt.commit();*/

                                //String msg = "Thank you for registering to Allergen Tracker! \nYour username is " + string_username + " and password is " + string_password;
                                //sendSMS(string_phonenumber, msg);

                                Toast.makeText(getApplicationContext(), "המשתמש " + usr.uName + " נוצר בהצלחה. נשלח מייל לאימות משתמש", Toast.LENGTH_SHORT).show();

                                finish();
                                }

                        }
                        @Override
                        public void onCancelled(DatabaseError error)
                        {
                            // Failed to read value
                            Toast.makeText(getApplicationContext(), "שגיאה", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else // invalid user
                {
                    Toast.makeText(getApplicationContext(), "לא נמצא משתמש לפי הנתונים שהוזנו", Toast.LENGTH_SHORT).show();
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

    public boolean validateUser(String username, String password, String email)
    {
        //check if user exists
        if(checkUsername(username) && checkPassword(password) && checkEmail(email))
            return true;
        else
            return false;
    }


    public boolean checkUsername(String p)
    {
        if(!p.isEmpty()) {
            if (!p.matches("^[a-zA-Z0-9._-]{3,}$"))
                return false;
            else
                return true;
        }
        else {
            username.setError("שדה זה לא יכול להיות ריק");
            username.requestFocus();
            return false;
        }
    }

    public boolean checkPassword(String p)
    {
        if(!p.isEmpty()) {
            int length = p.length();
            if (length < 5)
                return false;
            else
                return true;
        }
        else {
            password.setError("שדה זה לא יכול להיות ריק");
            password.requestFocus();
            return false;
        }
    }

    public boolean checkEmail(String p)
    {
        if(!p.isEmpty()) {
            if (!p.matches("^(.+)@(.+)$"))
                return false;
            else
                return true;
        }
        else {
            email.setError("שדה זה לא יכול להיות ריק");
            email.requestFocus();
            return false;
        }
    }
    private void sendEmailVerification(FirebaseAuth fAuth) {
        FirebaseUser firebaseUser = fAuth.getCurrentUser();

        firebaseUser.sendEmailVerification()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getApplicationContext(), "Instructions Sent...", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(getApplicationContext(), "Failed to send due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
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