package com.example.allergentrackerbeta;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.allergentrackerbeta.databinding.ActivityMenuBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
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

public class Menu extends DrawerBaseActivity {
    // views
    Button scan_product, add_product, logout;
    final static String USERNAME_KEY = "username";
    final static String PASSWORD_KEY = "password";
    TextView welcome;
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

        initViews(); // arrange Views

        //SharedPreferences prefs = getPreferences(Context.MODE_PRIVATE); // PreferenceManager.getDefaultSharedPreferences(this);
        //username.setText(prefs.getString(USERNAME_KEY, ""));
        //FirebaseDatabase database = FirebaseDatabase.getInstance();

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


        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initViews(); // arrange Views
                addButtonOn(false);
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
        welcome = findViewById(R.id.userText);
        welcome.setVisibility(View.INVISIBLE);
        logout = findViewById(R.id.logoutBtn);
        logout.setVisibility(View.INVISIBLE);
        add_product = findViewById(R.id.addBtn);
        add_product.setTextColor(Color.GRAY);
        scan_product = findViewById(R.id.scanBtn);
    }

    // position the views after logging in
    public void repositionButtons(String uName)
    {
        welcome.setVisibility(View.VISIBLE);
        logout.setVisibility(View.VISIBLE);

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
}