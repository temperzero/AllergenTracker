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
import android.view.SubMenu;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class DrawerBaseActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    TextView userNameText, emailText;
    FirebaseAuth fAuth;
    Toolbar toolbar;
    NavigationView navigationView;

    @Override
    public void setContentView(View view) {
        drawerLayout = (DrawerLayout)getLayoutInflater().inflate(R.layout.activity_drawer_base,null);
        FrameLayout container = drawerLayout.findViewById(R.id.activityContainer);
        container.addView(view);
        super.setContentView(drawerLayout);


        toolbar = drawerLayout.findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        navigationView = drawerLayout.findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        // saving the view id of the drawer text
        userNameText = navigationView.getHeaderView(0).findViewById(R.id.HeaderUserText);
        emailText = navigationView.getHeaderView(0).findViewById(R.id.HeaderEmailText);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.menu_open,R.string.menu_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        fAuth = FirebaseAuth.getInstance();

        // if user is logged in
        //boolean loggedIn = false;
        //if(fAuth.getCurrentUser() != null)
        //    loggedIn = true;
        LoggedInMenu(fAuth.getCurrentUser() != null); // true is user is logged in
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // close the drawer after selecting an item
        drawerLayout.closeDrawer(GravityCompat.START);
        int id = item.getItemId();
        switch(id)
        {
            case R.id.nav_home: {
                Intent homeIntent = new Intent(this, Menu.class);
                startActivity(homeIntent);
                overridePendingTransition(0,0);
                break;
            }
            case R.id.nav_add: {
                if(fAuth.getCurrentUser() != null)
                {
                    if(fAuth.getCurrentUser().isEmailVerified())
                    {
                        Intent addIntent = new Intent(this, AddProduct.class);
                        startActivity(addIntent);
                        overridePendingTransition(0, 0);
                    }
                    else
                        Toast.makeText(getApplicationContext(), "יש לאשר את המייל על מנת להוסיף מוצרים", Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getApplicationContext(), "הירשמו בכדי להוסיף מוצרים!", Toast.LENGTH_SHORT).show();
                break;
            }
            case R.id.nav_info: {
                Intent infoNexpertsIntent = new Intent(this, InfoAndExperts.class);
                startActivity(infoNexpertsIntent);
                overridePendingTransition(0,0);
                break;
            }
            case R.id.nav_signin: {
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
                fAuth.signOut();
                Intent logoutIntent = new Intent(this, Menu.class);
                startActivity(logoutIntent);
                overridePendingTransition(0,0);
                break;
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

    protected void UpdateUserTags(String userName, String email)
    {
            userNameText.setText(userName);
            emailText.setText(email);
    }

    protected void LoggedInMenu(boolean isLogged)
    {
        if(isLogged)
        {
            Toast.makeText(getApplicationContext(), "mehubar", Toast.LENGTH_SHORT).show();
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.main_menu_loggedin);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "not mehubar", Toast.LENGTH_SHORT).show();
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.main_menu);
        }
    }

}