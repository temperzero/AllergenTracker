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
    Button scan_product;
    Button searchProd;
    final static String USERNAME_KEY = "username";
    final static String PASSWORD_KEY = "password";
    // global variables
    boolean found = false; // used to check if product was found in DB
    // drawer menu binding
    ActivityMenuBinding activityMenuBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMenuBinding = ActivityMenuBinding.inflate(getLayoutInflater());
        setContentView(activityMenuBinding.getRoot());
        AllocateActivityTitle("מסך ראשי");

        scan_product = findViewById(R.id.scanBtn);
        searchProd = findViewById(R.id.searchProdAllergens);

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

        if(fAuth.getCurrentUser() != null) // if user is logged on from previous app use
            if(fAuth.getCurrentUser().isEmailVerified())
                UpdateUserTags(fAuth.getCurrentUser().getDisplayName(), fAuth.getCurrentUser().getEmail());
            else
                UpdateUserTags("אורח", "");
        else
            UpdateUserTags("אורח", "");

        searchProd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search = new Intent(Menu.this, SearchProduct.class);
                startActivity(search);

            }
        });
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
                        alertDialogBuilder.setMessage("האם תרצו להוסיף את המוצר ידנית?")

                                .setCancelable(false).setPositiveButton("הוסף", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sp = getSharedPreferences("com.example.allergentrackerbeta", 0);
                                SharedPreferences.Editor sedt = sp.edit();
                                sedt.putString("barcode", intentResult.getContents());
                                sedt.commit();
                                Intent addProduct = new Intent(Menu.this, NewAddProduct.class);
                                startActivity(addProduct);
                            }
                        })
                                .setNegativeButton("ביטול", new DialogInterface.OnClickListener() {
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
                    Toast.makeText(getApplicationContext(), "שגיאה לא צפויה", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
            //when result content is null
            Toast.makeText(getApplicationContext(), "שגיאה בסריקה! נסו שוב",Toast.LENGTH_SHORT).show();
    }
}