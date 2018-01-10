package com.example.augappprototype.Listeners;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.augappprototype.LoginScreen;
import com.example.augappprototype.MainActivity;
import com.example.augappprototype.R;

import java.util.HashMap;

/**
 * Created by Pao on 1/7/2018.
 */

public class LoginButtonListener implements View.OnClickListener{

    /*--Data--*/
    HashMap<String, String> permissions = new HashMap<>();
    HashMap<String, String> passwords = new HashMap<>();
    private final LoginScreen loginScreen;

    /*--Constructor--*/
    public LoginButtonListener(LoginScreen loginScreen){
        this.loginScreen = loginScreen;
    }//LoginButtonListener

    /*--Methods--*/
    @Override
    /**
     * Calls the addName and userNamePasswordToString methods in order to check the username
     * and password and will either log the user in or display a toast saying Login Failed
     */
    public void onClick(View v) {
        addName();
        userNamePasswordToString();
    }//onClick

    /**
     * addName() --> void
     * Adds the name and password to the passwords hash map and adds the name and role in the
     * permissions hash map
     */
    public void addName(){
        permissions.put("paolo", "student");
        permissions.put("zach", "student");
        passwords.put("vpreyes@ualberta.ca", "123");
        passwords.put("frithsmi@ualberta.ca", "123");
    }//addName

    /**
     * confirmLogin(String, String) --> void
     * Checks if the username and password are correct and checks if the login is a student or
     * faculty member and then if the password and username match it will go to the main menu
     * @param username
     * @param password
     */
    public void confirmLogin(String username, String password){
        if (password.equals(passwords.get(username))){
            Intent k = new Intent(loginScreen, MainActivity.class);
            loginScreen.startActivity(k);
            if (permissions.get(username).equals("faculty"))
                Toast.makeText(loginScreen, "Logged in as a faculty member",
                        Toast.LENGTH_LONG).show();
            else
                Toast.makeText(loginScreen, "Logged in as a student",
                        Toast.LENGTH_LONG).show();
        }//if
        else{
            Toast.makeText(loginScreen, "Login Failed!",
                    Toast.LENGTH_LONG).show();
        }//else
    }//confirmLogin

    /**
     * userNamePasswordToString() --> void
     * Changes the username and password to a string
     * Calls the confirmLogin method that checks if the username and password match and checks
     * if the email is student or faculty
     */
    public void userNamePasswordToString(){
        EditText username = loginScreen.findViewById(R.id.userEmail);
        EditText password = loginScreen.findViewById(R.id.userPassword);
        String usernameString = username.getText().toString();
        String passwordString = password.getText().toString();
        confirmLogin(usernameString, passwordString);
    }//userNamePasswordToString
}//LoginButtonListener
