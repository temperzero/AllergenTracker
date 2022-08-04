package com.example.allergentrackerbeta;

import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
//TODO: investigate why login is successful when email is not yet validated, registration with an existing email crashes the app,
// navigate back to main menu without register and login buttons
public class Register extends AppCompatActivity {
    // views
    Button registerBtn, back;
    EditText username, email, password;
    FirebaseAuth fAuth; //firebase Authentication instance

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

                // check if the user credentials are legal
                if(validateUser(string_username, string_password, string_email))
                {
                    register(string_email, string_password);

                    //FirebaseDatabase database = FirebaseDatabase.getInstance();
                    //DatabaseReference users = database.getReference("Users"); //.child(string_username);
//                    users.addListenerForSingleValueEvent(new ValueEventListener() {
//                        @Override
//                        public void onDataChange(DataSnapshot dataSnapshot)
//                        {
//                            if (dataSnapshot.child(string_username).exists()) // username  exists
//                                Toast.makeText(getApplicationContext(), "שם משתמש תפוס", Toast.LENGTH_SHORT).show();
//                            if( dataSnapshot.child(string_email).exists()) // abc@mail.com abcmailcom
//                                Toast.makeText(getApplicationContext(), "כתובת אימייל תפוסה", Toast.LENGTH_SHORT).show(); //email exists
//                            else //username doesn't exist
//                            {
//                                User usr = new User(string_username, string_password, string_email);
//                                DatabaseReference userToAdd = database.getReference("Users").child(usr.uName);
//                                //sendEmailVerification( fAuth );
//                                userToAdd.setValue(usr);
//
//                                /*// User object into json and save in shared prefrences
//                                Gson gson = new Gson();
//                                String json = gson.toJson(usr);
//                                sedt.putString("User",json);
//                                sedt.commit();*/
//
//                                //String msg = "Thank you for registering to Allergen Tracker! \nYour username is " + string_username + " and password is " + string_password;
//                                //sendSMS(string_phonenumber, msg);
//
//                                Toast.makeText(getApplicationContext(), "המשתמש " + usr.uName + " נוצר בהצלחה. נשלח מייל לאימות משתמש", Toast.LENGTH_SHORT).show();
//
//                                finish();
//                                }
//
//                        }
//                        @Override
//                        public void onCancelled(DatabaseError error)
//                        {
//                            // Failed to read value
//                            Toast.makeText(getApplicationContext(), "שגיאה", Toast.LENGTH_SHORT).show();
//                        }
//                    });
                }
                else // invalid user
                {
                    Toast.makeText(getApplicationContext(), "נתונים לא תקינים", Toast.LENGTH_SHORT).show();
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
        //check if user input is eligible
        if(checkUsername(username) && checkPassword(password) && checkEmail(email))
            return true;
        else
            return false;
    }


    public boolean checkUsername(String p)
    {
        if(!p.isEmpty()) {
            if (!p.matches("^[a-zA-Z0-9._-]{3,}$")) {
                username.setError("שם משתמש לא תקין");
                username.requestFocus();
                return false;
            }
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
            if (length < 5) {
                password.setError("הססמא צריכה להכיל מינימום 6 תווים");
                password.requestFocus();
                return false;
            }
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
            if (!p.matches("[a-z0-9_]+@[a-z]+\\.[a-z]{2,3}")) {
                email.setError("כתובת מייל לא תקינה");
                email.requestFocus();
                return false;
            }
            else
                return true;
        }
        else {
            email.setError("שדה זה לא יכול להיות ריק");
            email.requestFocus();
            return false;
        }
    }
    //registration process
    private void register(String email, String password){
        Task<AuthResult> registerTask = fAuth.createUserWithEmailAndPassword(email, password);
        registerTask.addOnCompleteListener(this, new RegisterCompleteListener());
    }
    // implementation of the complete registration interface
    class RegisterCompleteListener implements OnCompleteListener<AuthResult>{
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){
                Toast.makeText(getApplicationContext(), "ההרשמה הושלמה בהצלחה", Toast.LENGTH_SHORT).show();

                FirebaseUser firebaseUser = fAuth.getCurrentUser();
                firebaseUser.sendEmailVerification()
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(getApplicationContext(), "קישור לאימות המשתמש נשלח לכתובת המייל", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                assert user != null;
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName(username.getText().toString()).build();
                                user.updateProfile(profileUpdates);
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(getApplicationContext(), "Failed to send due to "+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }else{
                try{
                    String error = task.getResult().toString();
                    Toast.makeText(getApplicationContext(), "תהליך ההרשמה נכשל. " + error, Toast.LENGTH_SHORT).show();
                }catch(Exception ex){
                    if(ex.getCause() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(getApplicationContext(), "כתובת המייל שהוזנה כבר נמצאת בשימוש" , Toast.LENGTH_SHORT).show();
                    }else if (ex.getCause() instanceof FirebaseAuthWeakPasswordException){
                        Toast.makeText(getApplicationContext(), "כתובת המייל שהוזנה כבר נמצאת בשימוש" , Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }
    }
}