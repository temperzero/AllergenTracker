package com.example.allergentrackerbeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignUp extends AppCompatActivity {

    Button login, signup;
    TextInputEditText username, email, password;
    FirebaseAuth fAuth; //firebase Authentication instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //disable night mode display
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // action bar initialization
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Set BackgroundDrawable
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#5C9CED"));
        actionBar.setBackgroundDrawable(colorDrawable);
        // set actionbar title
        actionBar.setTitle("הרשמה");

        login = findViewById(R.id.signup_login);
        signup = findViewById(R.id.signupButton);
        username = findViewById(R.id.signup_username);
        email = findViewById(R.id.signup_email);
        password = findViewById(R.id.signup_password);
        fAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent LoginIntent = new Intent(SignUp.this, Login.class);
                startActivity(LoginIntent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
    }

    // check whether all user credentials are valid
    public boolean validateUser(String username, String password, String email)
    {
        return (CheckInput.checkUsername(username, this.username) && CheckInput.checkEmail(email, this.email) && CheckInput.checkPassword(password, this.password) );
    }

    //registration process
    private void register(String email, String password){
        //create a register task and add on complete listener interface
        Task<AuthResult> registerTask = fAuth.createUserWithEmailAndPassword(email, password);
        registerTask.addOnCompleteListener(this, new SignUp.RegisterCompleteListener());
    }

    // implementation of the complete registration interface
    class RegisterCompleteListener implements OnCompleteListener<AuthResult> {
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
                                // logout before moving to main menu
                                fAuth.signOut();
                                Intent MenuIntent = new Intent(SignUp.this, Menu.class);
                                startActivity(MenuIntent);
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(Exception e) {
                                Toast.makeText(getApplicationContext(), "לא ניתן לשלוח מייל, יש לוודא שהכתובת תקינה", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
            // if register task failed, catch the exception
            else{
                try{
                    String error = task.getResult().toString();
                    Toast.makeText(getApplicationContext(), "תהליך ההרשמה נכשל" + error, Toast.LENGTH_SHORT).show();
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

    // back button enabled
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        Intent MenuIntent = new Intent(SignUp.this, Menu.class);
        startActivity(MenuIntent);
    }
}