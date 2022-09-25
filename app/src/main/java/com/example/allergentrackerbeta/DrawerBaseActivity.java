package com.example.allergentrackerbeta;

import android.content.Intent;
import android.net.Uri;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

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

        LoggedInMenu(fAuth.getCurrentUser() != null); // true is user is logged in
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // close the drawer after selecting an item
        drawerLayout.closeDrawer(GravityCompat.START);
        int id = item.getItemId();
        switch(id)
        {
            case R.id.nav_add: {
                if(fAuth.getCurrentUser() != null)
                {
                    if(fAuth.getCurrentUser().isEmailVerified())
                    {
                        Intent addIntent = new Intent(this, NewAddProduct.class);
                        startActivity(addIntent);
                        //overridePendingTransition(0, 0);
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
                //overridePendingTransition(0,0);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            }
            case R.id.nav_contact: {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:" + "allergentracker@gmail.com"));
                intent.putExtra(Intent.EXTRA_SUBJECT, "string_subject");
                intent.putExtra(Intent.EXTRA_TEXT, "string_msg");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                try {
                    startActivity(Intent.createChooser(intent, "בחר אפליקציה לשליחת מייל..."));
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(), "לא נמצאו אפליקציות מתאימות", Toast.LENGTH_SHORT).show();
                }
                break;
            }
            case R.id.nav_signin: {
                Intent LoginIntent = new Intent(this, Login.class);
                startActivity(LoginIntent);
                //overridePendingTransition(0,0);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            }
            case R.id.nav_signup: {
                Intent registerIntent = new Intent(this, SignUp.class);
                startActivity(registerIntent);
                //overridePendingTransition(0,0);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            }
            case R.id.nav_reset: {
                String email = fAuth.getCurrentUser().getEmail().toString();
                Task<Void> resetPassTask = fAuth.sendPasswordResetEmail(email);
                resetPassTask.addOnCompleteListener(new DrawerBaseActivity.ForgotPassCompleteListener());
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
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.main_menu_loggedin);
        }
        else
        {
            navigationView.getMenu().clear();
            navigationView.inflateMenu(R.menu.main_menu);
        }
    }

    class ForgotPassCompleteListener implements OnCompleteListener<Void> {

        @Override
        public void onComplete(@NonNull Task<Void> task) {
            if(task.isSuccessful()) {
                Toast.makeText(getApplicationContext(), "קישור לאיפוס הסיסמה נשלח אל כתובת האימייל", Toast.LENGTH_SHORT).show();
                fAuth.signOut();
                Intent menu = new Intent(DrawerBaseActivity.this, Menu.class);
                startActivity(menu);
            }
            else {
                try { throw task.getException(); }
                catch(FirebaseAuthInvalidCredentialsException e)
                {
                    Toast.makeText(getApplicationContext(), "בעיה בשליחת קישור לאיפוס הסיסמה לכתובת האימייל", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(getApplicationContext(), "בעיה בשליחת קישור לאיפוס הסיסמה לכתובת האימייל2", Toast.LENGTH_SHORT).show();
                }
            }
        }

    }
}