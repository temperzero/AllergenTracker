package com.example.allergentrackerbeta;


import com.google.android.material.textfield.TextInputEditText;

public class CheckInput {

    // class contains the check input functions for SignUp, Login and PasswordRecovery classes

    //check if username is valid
    public static boolean checkUsername(String p, TextInputEditText username)
    {
        if(!p.isEmpty()) {
            if (!p.matches("^[a-zA-Z]([0-9_-](?![_-])|[a-zA-Z0-9]){2,}$")) {

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
                password.setError("הסיסמא צריכה להכיל  6 תווים לפחות");
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
            if (!p.matches("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")) {
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
