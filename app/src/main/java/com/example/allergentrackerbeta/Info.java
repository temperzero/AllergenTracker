package com.example.allergentrackerbeta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Info extends AppCompatActivity {
    Button back;
    Button next;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        back = findViewById(R.id.backBtn);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });

        next = findViewById(R.id.nextBtn);
        next.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent showIntent = new Intent(Info.this, Experts.class);
                startActivity(showIntent);
            }
        });

        TextView Website = (TextView)findViewById(R.id.Website);
        Website.setMovementMethod(LinkMovementMethod.getInstance());
    }
}