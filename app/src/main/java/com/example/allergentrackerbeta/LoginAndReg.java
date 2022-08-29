package com.example.allergentrackerbeta;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

public class LoginAndReg  extends AppCompatActivity {

    // class contains the check input functions for SignUp and Login classes

    //check if username is valid
    public boolean checkUsername(String p, TextInputEditText username)
    {
        if(!p.isEmpty()) {
            if (!p.matches("^[a-zA-Z0-9._-]{3,}$")) {
                username.setError("שם משתמש לא תקין");
                username.requestFocus();
                return false;
            }
            else
                return true;
        }
        else {
            username.setError("שדה זה לא יכול להיות ריק");
            username.requestFocus();
            return false;
        }
    }

    //check if password is valid
    public static boolean checkPassword(String p, TextInputEditText password)
    {
        if(!p.isEmpty()) {
            int length = p.length();
            if (length < 5) {
                password.setError("הסיסמא צריכה להכיל לפחות 6 תווים");
                password.requestFocus();
                return false;
            }
            else
                return true;
        }
        else {
            password.setError("שדה זה לא יכול להיות ריק");
            password.requestFocus();
            return false;
        }
    }

    //check if email is valid
    public static boolean checkEmail(String p, TextInputEditText email )
    {
        if(!p.isEmpty()) {
            if (!p.matches("[a-z0-9_]+@[a-z]+\\.[a-z]{2,3}")) {
                email.setError("כתובת מייל לא תקינה");
                email.requestFocus();
                return false;
            }
            else
                return true;
        }
        else {
            email.setError("שדה זה לא יכול להיות ריק");
            email.requestFocus();
            return false;
        }
    }
}
