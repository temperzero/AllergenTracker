package com.example.allergentrackerbeta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class Experts extends AppCompatActivity {
    ImageView clalit;
    ImageView maccabi;
    ImageView meuhedet;
    ImageView leumit;
    Button back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experts);


        clalit = findViewById(R.id.clalit);
        clalit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent openlinkclalit = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.clalit.co.il/he/sefersherut/Pages/services_guide.aspx"));
                startActivity(openlinkclalit);
            }
        });

        maccabi = findViewById(R.id.maccabi);
        maccabi.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent openlinkmaccabi = new Intent(Intent.ACTION_VIEW, Uri.parse("https://serguide.maccabi4u.co.il/heb/doctors/doctorssearchresults/?Field=098&PageNumber=1&RequestId=00000000-0000-0001-0000-000000000098&SelectedTab=1"));
                startActivity(openlinkmaccabi);
            }
        });

        leumit = findViewById(R.id.leumit);
        leumit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent openlinkleumit = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.leumit.co.il/heb/ServiceSearch/doctorSearch/"));
                startActivity(openlinkleumit);
            }
        });

        meuhedet = findViewById(R.id.meuhedet);
        meuhedet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Intent openlinkmeuhedet = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.meuhedet.co.il/search?mod=115&rn=18716%22%7D"));
                startActivity(openlinkmeuhedet);
            }
        });

        back = findViewById(R.id.backtoinfoBtn);
        back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                finish();
            }
        });
    }
}