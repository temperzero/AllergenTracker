package com.example.allergentrackerbeta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.allergentrackerbeta.databinding.ActivityMenuBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.sql.SQLOutput;

public class Menu extends DrawerBaseActivity {
    // views
    Button scan_product, add_product, register, login, logout;
    EditText username, password;
    final static String USERNAME_KEY = "username";
    final static String PASSWORD_KEY = "password";
    TextView welcome;
    TextView userNameText;
    // global variables
    boolean found = false; // used to check if product was found in DB
    FirebaseAuth fAuth;
    // drawer menu binding
    ActivityMenuBinding activityMenuBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMenuBinding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(activityMenuBinding.getRoot());
        AllocateActivityTitle("מסך ראשי");
        //setContentView(R.layout.activity_menu);

        //SharedPreferences sp = getSharedPreferences("com.example.allergentrackerbeta", 0 );
        //SharedPreferences.Editor sedt = sp.edit ();

        initViews(); // arrange Views

        SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE); // PreferenceManager.getDefaultSharedPreferences(this);
        username.setText(prefs.getString(USERNAME_KEY, ""));
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        fAuth = FirebaseAuth.getInstance();

        //scan button
        scan_product.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //init intent integrator
                IntentIntegrator intentIntegrator = new IntentIntegrator(Menu.this);
                //set prompt text -- show message
                intentIntegrator.setPrompt("לפלאש השתמש בכפתור הווליום");
                //set beep
                intentIntegrator.setBeepEnabled(true);
                //locked orientation
                intentIntegrator.setOrientationLocked(true);
                //set capture activity
                intentIntegrator.setCaptureActivity(Capture.class);
                //init scan
                intentIntegrator.initiateScan();
            }
        });

        //register button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent regIntent = new Intent(Menu.this, Register.class);
                startActivity(regIntent);
            }
        });

        // login button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

//                user.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        if (dataSnapshot.exists()) // username exists
//                        {
//                            User a = dataSnapshot.getValue(User.class);
//                            if (a.uPass.equals(string_password)) // check if password is correct
//                            {
//                                repositionButtons(a.uName);
//
//                                addButtonOn(true);
//
//                                // User object into json and save in shared prefrences
//                                Gson gson = new Gson();
//                                String json = gson.toJson(a);
//                                sedt.putString("User",json);
//                                sedt.commit();
//
//                                Toast.makeText(getApplicationContext(), "התחברת בהצלחה", Toast.LENGTH_SHORT).show();
//                            }
//                            else
//                                Toast.makeText(getApplicationContext(), "טעות בפרטי המשתמש", Toast.LENGTH_SHORT).show();
//
//                        } else //username doesn't exist
//                        {
//                            Toast.makeText(getApplicationContext(), "טעות בפרטי המשתמש", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError error) {
//                        // Failed to read value
//                        Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initViews(); // arrange Views
                addButtonOn(false);
                password.getText().clear();
                fAuth.signOut();
            }
        });

        if(fAuth.getCurrentUser() != null) // if user is logged on from previous app use
        {
            repositionButtons(fAuth.getCurrentUser().getDisplayName());
            addButtonOn(true); // add product button enabled
            // test
            UpdateUserName(fAuth.getCurrentUser().getDisplayName());
        }
        else
            addButtonOn(false);  // add product button disabled
    }

    // activated after scanning a barcode in Scan button
    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data)
    {
        // firebase instance
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        //dialog initialize
        final Context context = this;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

        super.onActivityResult(requestCode, resultCode, data);
        //initialize intent result
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        //check if a barcode has been identified (string)
        if (intentResult.getContents() != null)
        {
            DatabaseReference products = ref.child("Products");
            products.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot)
                {
                    for (DataSnapshot child: dataSnapshot.getChildren())
                    {
                        Product p = child.getValue(Product.class);
                        if(p.barcode.equals(intentResult.getContents())) // check if barcode is equal
                        {
                            // set dialog box
                            alertDialogBuilder.setTitle("מוצר נמצא");
                            alertDialogBuilder.setMessage("\nשם המוצר: " + p.pName+ "\nשם החברה: " + p.cName +"\nאלרגנים: " + p.allergens)
                                    .setCancelable(false).setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                            found = true;
                            break;
                        }
                    }
                    if(!found)
                    {
                        // set dialog box
                        alertDialogBuilder.setTitle("מוצר לא נמצא");
                        alertDialogBuilder.setMessage("לחץ אישור ליציאה")
                                .setCancelable(false).setPositiveButton("אישור", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                    }
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    found = false; // reset for next product scan
                }
                @Override
                public void onCancelled(DatabaseError error)
                {
                    // Failed to read value
                    Toast.makeText(getApplicationContext(), "error", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
            //when result content is null
            Toast.makeText(getApplicationContext(), "שגיאה בסריקה! נסו שוב",Toast.LENGTH_SHORT).show();
    }

    // starting views positioning
    public void initViews()
    {
        username = findViewById(R.id.loginUsername);
        username.setVisibility(View.VISIBLE);
        password = findViewById(R.id.loginPassword);
        password.setVisibility(View.VISIBLE);
        welcome = findViewById(R.id.userText);
        welcome.setVisibility(View.INVISIBLE);
        logout = findViewById(R.id.logoutBtn);
        logout.setVisibility(View.INVISIBLE);
        login = findViewById(R.id.loginBtn);
        login.setVisibility(View.VISIBLE);
        register = findViewById(R.id.regBtn);
        register.setVisibility(View.VISIBLE);
        add_product = findViewById(R.id.addBtn);
        add_product.setTextColor(Color.GRAY);
        scan_product = findViewById(R.id.scanBtn);
    }

    // position the views after logging in
    public void repositionButtons(String uName)
    {
        welcome.setVisibility(View.VISIBLE);
        logout.setVisibility(View.VISIBLE);

        login.setVisibility(View.INVISIBLE);
        register.setVisibility(View.INVISIBLE);
        username.setVisibility(View.INVISIBLE);
        password.setVisibility(View.INVISIBLE);

        welcome.setText("ברוך הבא " + uName);
        welcome.setTextColor(Color.WHITE);

        add_product.setTextColor(Color.BLACK);
    }

    // add button on/off
    public void addButtonOn(boolean on)
    {
        if(on)
        {
            add_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent addIntent = new Intent(Menu.this, AddProduct.class);
                    startActivity(addIntent);
                }
            });
        }
        else
            {
            add_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), "על מנת להוסיף מוצרים למאגר, יש צורך בהרשמה או התחברות", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    class LoginCompleteListener implements OnCompleteListener<AuthResult> {

        @Override
        public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()) {
                FirebaseUser login = fAuth.getCurrentUser();
                if (login.isEmailVerified()) {
                    repositionButtons(login.getDisplayName());
                    addButtonOn(true);
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