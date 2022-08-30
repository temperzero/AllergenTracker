package com.example.allergentrackerbeta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.view.GravityCompat;

import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    TextView userNameText;

    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout)getLayoutInflater().inflate(R.layout.activity_drawer_base,null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);


        Toolbar toolbar = drawerLayout.findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        NavigationView navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // saving the view id of the drawer text
        userNameText = navigationView.getHeaderView(0).findViewById(R.id.HeaderUserText);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.menu_open,R.string.menu_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // close the drawer after selecting an item
        drawerLayout.closeDrawer(GravityCompat.START);
        switch(item.getItemId())
        {
            case R.id.nav_home: {
                Intent homeIntent = new Intent(this, Menu.class);
                startActivity(homeIntent);
                overridePendingTransition(0,0);
                break;
            }
            case R.id.nav_add: {
                Intent addIntent = new Intent(this, AddProduct.class);
                startActivity(addIntent);
                overridePendingTransition(0,0);
                break;
            }
            case R.id.nav_info: {
                Intent infoNexpertsIntent = new Intent(this, InfoAndExperts.class);
                startActivity(infoNexpertsIntent);
                overridePendingTransition(0,0);
                break;
            }
            case R.id.nav_register: {
                Intent LoginIntent = new Intent(this, Login.class);
                startActivity(LoginIntent);
                overridePendingTransition(0,0);
                break;
            }
            case R.id.nav_signup: {
                Intent registerIntent = new Intent(this, SignUp.class);
                startActivity(registerIntent);
                overridePendingTransition(0,0);
                break;
            }
            case R.id.nav_logout: {
                Toast.makeText(getApplicationContext(), "nothing happens", Toast.LENGTH_SHORT).show();
            }
            default:
            {
                Toast.makeText(getApplicationContext(), "??????", Toast.LENGTH_SHORT).show();
            }
        }
        return false;
    }

    // the title of the activity
    protected void AllocateActivityTitle(String titleString) {
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle(titleString);
        }
    }

    protected void UpdateUserName(String userName)
    {
        if(userNameText == null)
            Toast.makeText(getApplicationContext(), userName, Toast.LENGTH_SHORT).show();
        else
            userNameText.setText("ברוך הבא " + userName);
    }

}