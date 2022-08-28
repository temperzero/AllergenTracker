package com.example.allergentrackerbeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;

import com.example.allergentrackerbeta.databinding.ActivityMenuBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class Login extends AppCompatActivity {

    Button login, goToRegister, forgotPassword;
    TextInputEditText username, password;

    FirebaseAuth fAuth;

    final static String USERNAME_KEY = "username";
    final static String PASSWORD_KEY = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // action bar initialization
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Set BackgroundDrawable
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#5C9CED"));
        actionBar.setBackgroundDrawable(colorDrawable);
        // set actionbar title
        actionBar.setTitle("התחברות");

        username = findViewById(R.id.login_username);
        password = findViewById(R.id.login_password);
        login = findViewById(R.id.loginButton);

        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE); // PreferenceManager.getDefaultSharedPreferences(this);
        //username.setText(prefs.getString(USERNAME_KEY, ""));
        //FirebaseDatabase database = FirebaseDatabase.getInstance();

        fAuth = FirebaseAuth.getInstance();

        // init dialog box for testing
        final Context context = this;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        // login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // dialog test
                alertDialogBuilder.setTitle("פרטי משתמש");
                alertDialogBuilder.setMessage("שם משתמש: " + username.getText().toString() + "\n סיסמה: " + password.getText().toString())
                        .setCancelable(false).setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                // end test


                // the real deal
                SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE);
                String string_username = username.getText().toString();
                String string_password = password.getText().toString();
                if(!(Register.checkPassword(string_password,password ) &&  Register.checkEmail(string_username,username)))
                    return;
                Task<AuthResult> loginTask = fAuth.signInWithEmailAndPassword(string_username, string_password);
                loginTask.addOnCompleteListener((Activity) view.getContext(), new LoginCompleteListener());
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(USERNAME_KEY, string_username);
                editor.putString(PASSWORD_KEY, string_password);
                editor.apply();
            }
        });

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

    class LoginCompleteListener implements OnCompleteListener<AuthResult> {

        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()) {
                FirebaseUser login = fAuth.getCurrentUser();
                if (login.isEmailVerified()) {
                    Toast.makeText(getApplicationContext(), "ההתחברות בוצעה בהצלחה", Toast.LENGTH_SHORT).show();
                }
                else Toast.makeText(getApplicationContext(), "יש לאמת את המשתמש לפני התחברות", Toast.LENGTH_SHORT).show();
            }
            else {
                try { throw task.getException(); }
                catch(FirebaseAuthInvalidCredentialsException e){ Toast.makeText(getApplicationContext(), "ההתחברות נכשלה, אחד או יותר מהפרטים שהזנת שגויים", Toast.LENGTH_SHORT).show(); }
                catch (Exception e) {}
            }
        }
    }
}