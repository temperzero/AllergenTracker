package com.example.allergentrackerbeta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchProduct extends AppCompatActivity {

    TextView productsNotFound;
    TextInputEditText searchBox;
    ImageButton searchBtn;
    ListView prodAllergensList;
    ArrayList<Product> productsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //disable night mode display
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        // action bar initialization
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        // Set BackgroundDrawable
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#5C9CED"));
        actionBar.setBackgroundDrawable(colorDrawable);
        // set actionbar title
        actionBar.setTitle("חיפוש מוצר");

        setContentView(R.layout.activity_new_search_product);
        searchBox = findViewById(R.id.searchProduct);
        searchBtn = findViewById(R.id.searchProductBtn);
        productsNotFound = findViewById(R.id.productNotFound);
        prodAllergensList = findViewById(R.id.productAllergens);

        productsNotFound.setVisibility(View.INVISIBLE);
        productsList = new ArrayList<Product>();
        ArrayAdapter<Product> productsAdapter = new ArrayAdapter<Product>(this, android.R.layout.simple_list_item_1, productsList);
        prodAllergensList.setAdapter(productsAdapter);

        searchBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String productName = searchBox.getText().toString();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                productsList.clear();
                productsAdapter.notifyDataSetChanged(); //notify for searching a different product
                productsNotFound.setVisibility(View.INVISIBLE);
                if (!productName.isEmpty())
                {
                    // close phone's keyboard
                    InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                    Query q = ref.child("Products").orderByChild("pName").startAt(productName).endAt(productName + "\uf8ff");
                    q.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            Product p = snapshot.getValue(Product.class);
                            productsList.add(p);
                            productsAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot snapshot) { }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) { }
                    });
                    //an Event called after all onChildAdded events of addChildEventListener finishes
                    q.addValueEventListener (new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (productsList.size() == 0)
                                productsNotFound.setVisibility(View.VISIBLE);
                        }
                        @Override public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
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

    @Override
    public void onBackPressed() {
        finish();
    }
}