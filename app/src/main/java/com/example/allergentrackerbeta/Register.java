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
import android.widget.TextView;
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

public class Register extends AppCompatActivity {
    // views
    Button registerBtn, back;
    EditText username, email, password;
    FirebaseAuth fAuth; //firebase Authentication instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
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

                // check if the user credentials are legal, if they are continue to registration process
                if(validateUser(string_username, string_password, string_email))
                    register(string_email, string_password);

                else // invalid user
                    Toast.makeText(getApplicationContext(), "ההרשמה נכשלה, נתונים לא תקינים", Toast.LENGTH_SHORT).show();
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

    // check whether all user credentials are valid
    public boolean validateUser(String username, String password, String email)
    {
        return (checkUsername(username) && checkPassword(password, this.password) && checkEmail(email, this.email));
    }

    //check if username is valid
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

    //check if password is valid
    public static boolean checkPassword(String p, TextView password)
    {
        if(!p.isEmpty()) {
            int length = p.length();
            if (length < 5) {
                password.setError("הססמא צריכה להכיל לפחות 6 תווים");
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

    //check if email is valid
    public static boolean checkEmail(String p, TextView email )
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
        //create a register task and add on complete listener interface
        Task<AuthResult> registerTask = fAuth.createUserWithEmailAndPassword(email, password);
        registerTask.addOnCompleteListener(this, new RegisterCompleteListener());
    }
    // implementation of the complete registration interface
    class RegisterCompleteListener implements OnCompleteListener<AuthResult>{
        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            //check if register task succeeded, if so, registration process succeeded, send verification email to the new user
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
                                Toast.makeText(getApplicationContext(), "לא ניתן לשלוח את מייל, יש לוודא שכתובת המייל תקינה", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            // if register task failed, catch the exception
            else{
                try{
                    String error = task.getResult().toString();
                    Toast.makeText(getApplicationContext(), "תהליך ההרשמה נכשל. " + error, Toast.LENGTH_SHORT).show();
                }
                catch(Exception ex){
                    if(ex.getCause() instanceof FirebaseAuthUserCollisionException)
                        Toast.makeText(getApplicationContext(), "כתובת המייל שהוזנה כבר נמצאת בשימוש" , Toast.LENGTH_SHORT).show();
                    //else if (ex.getCause() instanceof FirebaseAuthWeakPasswordException){
                    //    Toast.makeText(getApplicationContext(), "ססמא לא תקינה" , Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
    }