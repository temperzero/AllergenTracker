package com.example.allergentrackerbeta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SearchProduct extends AppCompatActivity {

    TextView searchBox, productsNotFound, textView;
    ImageButton searchBtn;
    ListView prodAllergensList;
    ArrayList<Product> productsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_product);

        searchBox = findViewById(R.id.searchProduct);
        searchBtn = findViewById(R.id.searchProductBtn);
        productsNotFound = findViewById(R.id.productNotFound);
        prodAllergensList = findViewById(R.id.productAllergens);
        productsNotFound.setVisibility(View.INVISIBLE);
        productsList = new ArrayList<Product>();
        textView = findViewById(R.id.textView);
        ArrayAdapter<Product> productsAdapter = new ArrayAdapter<Product>(this, android.R.layout.simple_list_item_1, productsList);
        prodAllergensList.setAdapter(productsAdapter);

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName =  searchBox.getText().toString();
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                productsList.clear();
                productsAdapter.notifyDataSetChanged();
                Query q = ref.child("Products").orderByChild("pName").startAt(productName).endAt(productName + "\uf8ff");
                q.addChildEventListener(new  ChildEventListener(){
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                        Product p = snapshot.getValue(Product.class);
                        productsList.add(p);
                        productsAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                if (productsList.size() == 0) //does not work properly due to asynchronous search operation onChildAdded
                    productsNotFound.setVisibility(View.VISIBLE);

            }

        });








    }
}