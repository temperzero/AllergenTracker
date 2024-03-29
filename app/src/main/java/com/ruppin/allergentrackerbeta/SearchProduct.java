package com.ruppin.allergentrackerbeta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchProduct extends AppCompatActivity {

    TextView productsNotFound;
    TextInputEditText searchBox;
    ImageButton searchBtn, speechToTextBtn;
    ListView prodAllergensList;
    ArrayList<Product> productsList;
    ArrayAdapter<Product> productsAdapter;

    private static final int REQUEST_CODE_SPEECH_INPUT = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //disable night mode display
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // action bar initialization
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Set BackgroundDrawable
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#63CEFF"));
        actionBar.setBackgroundDrawable(colorDrawable);
        // set actionbar title
        actionBar.setTitle("חיפוש מוצר");

        setContentView(R.layout.activity_new_search_product);
        searchBox = findViewById(R.id.searchProduct);
        searchBtn = findViewById(R.id.searchProductBtn);
        speechToTextBtn = findViewById(R.id.speechtotext);
        productsNotFound = findViewById(R.id.productNotFound);
        prodAllergensList = findViewById(R.id.productAllergens);

        productsNotFound.setVisibility(View.INVISIBLE);
        productsList = new ArrayList<Product>();
        productsAdapter = new ArrayAdapter<Product>(this, android.R.layout.simple_list_item_1, productsList);
        prodAllergensList.setAdapter(productsAdapter);

        searchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String productName = searchBox.getText().toString();
                searchProducts(productName);
            }
        });

        speechToTextBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // intent to start speech to text
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "he");
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "אמרו את שם המוצר");
                // start intent
                try
                {
                    startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "שגיאה בזיהוי הדיבור", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE_SPEECH_INPUT && resultCode == RESULT_OK) {
            searchBox.setText(data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0));
            searchProducts(searchBox.getText().toString());
        }
    }

    public void searchProducts(String productName) {

        if (!productName.isEmpty())
        {
            // close keyboard
            InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager.isAcceptingText())
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            if(productName.length() < 3) {
                searchBox.setError("שם המוצר בשדה החיפוש חייב להכיל לפחות שלוש אותיות");
                searchBox.requestFocus();
                return;
            }

        //check internet connection
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (!(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED) &&
                !(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED))
        {
            Toast.makeText(getApplicationContext(), "לא ניתן לגשת לבסיס הנתונים, אין חיבור לאינטרנט", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        productsList.clear();
        productsAdapter.notifyDataSetChanged(); //notify for searching a different product
        productsNotFound.setVisibility(View.INVISIBLE);

            // adds every product to the product list
            DatabaseReference products = ref.child("Products");
            products.addValueEventListener(new ValueEventListener()
            {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        Product p = child.getValue(Product.class);
                        // searches for instances where string is in the pName of products in the list
                        if ( p.pName.toUpperCase().contains(productName.toUpperCase()))
                            productsList.add(p);
                    }
                    productsAdapter.notifyDataSetChanged();
                    if (productsList.size() == 0)
                        productsNotFound.setVisibility(View.VISIBLE);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "שגיאה לא צפויה", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else {
            searchBox.setError("שורת החיפוש ריקה");
            searchBox.requestFocus();
        }
    }

    @Override
    protected void attachBaseContext(Context newBase)
    {
        final Configuration override = new Configuration(newBase.getResources().getConfiguration());
        override.fontScale = 1.0f;
        applyOverrideConfiguration(override);

        super.attachBaseContext(newBase);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
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
        finish();
    }
}