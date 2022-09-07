package com.example.allergentrackerbeta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class NewAddProduct extends AppCompatActivity {

    int num = 0; // number of products

    //init global buttons variables
    Button btScan;
    Button addToDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_add_product);

        //disable night mode display
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // action bar initialization
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Set BackgroundDrawable
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#0F5B91"));
        actionBar.setBackgroundDrawable(colorDrawable);
        // set actionbar title
        actionBar.setTitle("הוספת מוצר");

        TextView bc = findViewById(R.id.barcode);
        SharedPreferences sp = getSharedPreferences("com.example.allergentrackerbeta", 0);
        SharedPreferences.Editor sedt = sp.edit();
        String getBarcode = sp.getString("barcode",null);
        bc.setText(getBarcode);
        sedt.putString("barcode", "");
        sedt.commit();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        btScan = findViewById(R.id.bt_scan);
        // scan button click event
        btScan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //init intent integrator
                IntentIntegrator intentIntegrator = new IntentIntegrator(NewAddProduct.this);
                //set prompt text -- show message
                intentIntegrator.setPrompt("לפלאש השתמש בכפתור הגברת עוצמת הקול");
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

        DatabaseReference products = database.getReference("Products");
        products.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                num = (int) dataSnapshot.getChildrenCount();
            }
            @Override
            public void onCancelled(DatabaseError error)
            {
                // Failed to read value
                Toast.makeText(getApplicationContext(), "שגיאה לא צפויה", Toast.LENGTH_SHORT).show();
            }
        });

        // add to database button click event
        addToDB = findViewById(R.id.addbutton);
        addToDB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                EditText editP = findViewById(R.id.ProductName);
                String Pname = editP.getText().toString(); // get product name string
                EditText editC = findViewById(R.id.CompanyName);
                String Cname = editC.getText().toString(); // get company name string
                TextView text = findViewById(R.id.barcode);
                String BarcodeNum = text.getText().toString(); // get product name string

                // check allergens
                CheckBox Fish = findViewById(R.id.Fishbox);
                CheckBox Eggs = findViewById(R.id.EggsBox);
                CheckBox Seasame = findViewById(R.id.SeasameBox);
                CheckBox Sea = findViewById(R.id.SeaBox);
                CheckBox Wheat = findViewById(R.id.WheatBox);
                CheckBox Rye = findViewById(R.id.RyeBox);
                CheckBox Barley = findViewById(R.id.BarleyBox);
                CheckBox Oats = findViewById(R.id.OatsBox);
                CheckBox Milk = findViewById(R.id.MilkBox);
                CheckBox Peanuts = findViewById(R.id.PeanutBox);
                CheckBox Nuts = findViewById(R.id.NutsBox);
                CheckBox Soy = findViewById(R.id.SoyBox);

                // concatenate allergens
                String allergens = "";
                if(Fish.isChecked()) {
                    allergens = allergens + " דגים";
                    Fish.toggle();
                }
                if(Eggs.isChecked()) {
                    allergens = allergens + " ביצים";
                    Eggs.toggle();
                }
                if(Seasame.isChecked()) {
                    allergens = allergens + " שומשום";
                    Seasame.toggle();
                }
                if(Sea.isChecked()) {
                    allergens = allergens + " פירות ים";
                    Sea.toggle();
                }
                if(Wheat.isChecked()) {
                    allergens = allergens + " חיטה";
                    Wheat.toggle();
                }
                if(Rye.isChecked()) {
                    allergens = allergens + " שיפון";
                    Rye.toggle();
                }
                if(Barley.isChecked()) {
                    allergens = allergens + " שעורה";
                    Barley.toggle();
                }
                if(Oats.isChecked()) {
                    allergens = allergens + " שיבולת שועל";
                    Oats.toggle();
                }
                if(Milk.isChecked()) {
                    allergens = allergens + " חלב";
                    Milk.toggle();
                }
                if(Peanuts.isChecked()) {
                    allergens = allergens + " בוטנים";
                    Peanuts.toggle();
                }
                if(Nuts.isChecked()) {
                    allergens = allergens + " אגוזים";
                    Nuts.toggle();
                }
                if(Soy.isChecked()) {
                    allergens = allergens + " סויה";
                    Soy.toggle();
                }

                // check if barcode is empty
                if(!BarcodeNum.isEmpty())
                {
                    Product addProduct = new Product(Pname, Cname, BarcodeNum, allergens, num);
                    DatabaseReference productToAdd = database.getReference("Products").child("Product " + addProduct.pNum);

                    productToAdd.child("Products").child("Product " + addProduct.pNum);
                    productToAdd.setValue(addProduct);

                    Toast.makeText(getApplicationContext(), "שם החברה: " + addProduct.cName + "\nשם המוצר: " + addProduct.pName
                            + "\nברקוד: " + addProduct.barcode +"\nאלרגנים: " + allergens +"\nמס' מוצר: " + num, Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "ברקוד לא מזוהה", Toast.LENGTH_SHORT).show();

                // reset text
                editP.setText("");
                editC.setText("");
                text.setText("");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,@Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        //initialize intent result
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode,resultCode,data);
        //check condition
        if (intentResult.getContents() != null)
        {
            TextView text = findViewById(R.id.barcode);
            text.setText(intentResult.getContents());
            Toast.makeText(getApplicationContext(), "הסריקה בוצעה בהצלחה!",Toast.LENGTH_SHORT).show();
        }
        else
        {
            //when result content is null
            Toast.makeText(getApplicationContext(), "שגיאה בסריקה! נסו שוב",Toast.LENGTH_SHORT).show();
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
}