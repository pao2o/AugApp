package com.example.augappprototype;

/**
 * LoginScreen
 * extends AppCompatActivity
 * Is responsible for the Login Screen and all the buttons within the login screen
 * <p>
 * Methods:
 * onCreate(Bundle savedInstanceState)
 * Sets the content view as the login screen and calls the method that registers the listeners
 * for each button
 * registerListenersForLoginButtons()
 * Sets on click listeners for every button on the login screen
 */

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.augappprototype.Listeners.GuestButtonListener;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import org.w3c.dom.Text;

import java.util.ArrayList;

public class LoginScreen extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.OnConnectionFailedListener {

    TextView mOutputText;
    private Button signOutButton;
    private GoogleApiClient googleApiClient;
    private ImageView profilePicture;
    private LinearLayout profileSection;
    private SignInButton signInButton;
    private TextView email;
    private TextView name;
    private static final int REQUEST_CODE = 9001;
    ArrayList<String> whiteList = new ArrayList<String>();
    GoogleSignInAPI gsi;
    Context appContext = this;

    /*--Methods--*/

    /**
     * onCreate(Bundle) --> void
     * Calls the registerListenersForLoginScreenButtons method so there is a new on click listener
     * for them on creation
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        addName();
        registerListenersForLoginScreenButtons();

        profileSection = (LinearLayout) findViewById(R.id.profile_section);
        signOutButton = (Button) findViewById(R.id.logout_button);
        signInButton = (SignInButton) findViewById(R.id.login_button);
        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        profilePicture = (ImageView) findViewById(R.id.profile_picture);

       // mOutputText = findViewById(R.id.testTextView);
        signInButton.setOnClickListener(this);
        signOutButton.setOnClickListener(this);
        profileSection.setVisibility(View.GONE);

    }//onCreate

    /**
     * registerListenersForLoginScreenButtons() --> void
     * Sets on click listeners for the Login button and the Guest Login Button on the Login screen
     */
    public void registerListenersForLoginScreenButtons() {
        findViewById(R.id.continueAsGuestButton).setOnClickListener
                (new GuestButtonListener(this));
    }//registerListenersForLoginScreenButtons

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_button:
                signIn();
                break;
            case R.id.logout_button:
                signOut();
                break;
        } // switch(View)
    } // onClick(View)

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    } // onConnectionFailed(ConnectionResult)

    private void signIn() {
        gsi = new GoogleSignInAPI();
        Intent intent = new Intent(this, GoogleSignInAPI.class);
        startActivity(intent);
    } // signIn()

    private void signOut() {
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                updateUI(false);
            }
        });
    } // signOut()

    private void handleSignInResult(GoogleSignInResult result) {
        if(result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            String userName = account.getDisplayName();
            String userEmail = account.getEmail();
            name.setText(userName);
            email.setText(userEmail);
            checkPermissions(userEmail);
            Glide.with(this).load(account.getPhotoUrl()).into(profilePicture);
            updateUI(true);
        } else {
            updateUI(false);
        } // else
    } // handleSignInResult(GoogleSignInResult)

    private void updateUI(boolean isLogin) {
        if(isLogin) {
            profileSection.setVisibility(View.VISIBLE);
            signInButton.setVisibility(View.GONE);
        } else {
            profileSection.setVisibility(View.GONE);
            signInButton.setVisibility(View.VISIBLE);
        } // else
    } // updateUI

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE){
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        } // if
    } // onActivityResult(int, int, Intent)

    public void addName(){
        whiteList.add("shichun1@ualberta.ca");
        whiteList.add("vpreyes@ualberta.ca");
        whiteList.add("cwlarocq@ualberta.ca");
        whiteList.add("frithsmi@ualberta.ca");
    } // addName()

    public void checkPermissions(String email){
        if (whiteList.contains(email)) {
            Toast.makeText(this, "faculty",
                    Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "student",
                    Toast.LENGTH_LONG).show();
        } // else
        Intent goToMenu = new Intent(this, MainMenu.class);
        this.startActivity(goToMenu);
    } // checkPermissions(String)
}//LoginScreen