package com.example.dummy.helloworld;

import java.util.Collections;

import android.Manifest;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.view.View;
import android.accounts.Account;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;

public class MainActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 9001;

    private TextView textLog = null;
    private SignInButton signInBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);

        this.textLog = (TextView)findViewById(R.id.textLog);
        this.signInBtn = (SignInButton)findViewById(R.id.signInBtn);

        this.textLog.setText("Log:");

        MainActivity _this = this;

        this.signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _this.log("\n***\n");
                _this.log("Sign-in button clicked");
                _this.signIn();
            }
        });
    }

    private void log(String line) {
        this.textLog.setText(this.textLog.getText() + "\n" + line);
    }

    private GoogleSignInOptions gso = null;
    private GoogleSignInClient signInClient = null;
    private GoogleSignInAccount signedInAccount = null;
    private Account account = null;
    private GoogleAccountCredential credential = null;

    // Sign-in into a Google account for accessing Spreadsheets
    private boolean signIn() {
        this.log("signIn: BEGIN");

        try {

            this.log("signIn: GoogleSignInOptions.Builder");
            this.gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                // .requestIdToken(getString(R.string.server_client_id))
                // .requestServerAuthCode(getString(R.string.server_client_id))
                .build();

            this.log("signIn: GoogleSignIn.getClient");
            this.signInClient = GoogleSignIn.getClient(this, this.gso);

            this.log("signIn: GoogleSignIn.getLastSignedInAccount");
            this.signedInAccount = GoogleSignIn.getLastSignedInAccount(this);

            if (this.signedInAccount == null) {
                this.log("signIn: No previously authenticated Google account");
                this.nativeSignIn();
                this.log("signIn: The flow continues in a callback)");
            } else {
                this.log("signIn: Received previously authenticated Google account");
                this.obtainCredentials();
            }

        } catch (RuntimeException e) {
            this.log("FAILURE: signIn: " + e.getMessage());
            return false;
        }

        this.log("signIn: END");
        return true;
    }

    // A continuation flow of “this.signIn”.
    // Depends on readiness of “this.GoogleSignInClient”.
    private void obtainCredentials() {
        this.log("obtainCredentials: BEGIN");

        this.log("obtainCredentials: GoogleSignInAccount.getAccount");
        this.account = this.signedInAccount.getAccount();

        this.log("obtainCredentials: GoogleAccountCredential.usingOAuth2");
        this.credential = GoogleAccountCredential.usingOAuth2(
            this,
            Collections.singleton("https://www.googleapis.com/auth/spreadsheets")
        );

        // this.credentials = GoogleAccountCredential.usingOAuth2(this,
        //     Collections.singleton(DriveScopes.DRIVE_APPDATA)
        // ).setSelectedAccount(this.account)

        this.log("obtainCredentials: END");
    }

    // TODO
    // private Sheets service;
    /*private boolean mkService() {
        this.log("mkService: Begin");
        try {
            this.service =
                new Sheets.Builder(
                    httpTransport,
                    GsonFactory.getDefaultInstance(),
                    credential
                )
                .setApplicationName("Google Sheets API")
                .build();
        } catch (RuntimeException e) {
            this.log("FAILURE: mkService: " + e.getMessage());
            return false;
        }
        this.log("mkService: END");
        return true;
    }*/

    private void nativeSignIn() {
        this.log("nativeSignIn: BEGIN");

        this.log("nativeSignIn: GoogleSignInClient.getSignInIntent");
        Intent signInIntent = this.signInClient.getSignInIntent();

        this.log("nativeSignIn: startActivityForResult");
        startActivityForResult(signInIntent, RC_SIGN_IN);

        this.log("nativeSignIn: END");
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        this.log("onActivityResult: BEGIN");
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from “this.nativeSignIn”
        if (requestCode == RC_SIGN_IN) {
            this.log("onActivityResult: Matching RC_SIGN_IN");

            this.log("onActivityResult: GoogleSignIn.getSignedInAccountFromIntent");
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);

            MainActivity _this = this;

            task.addOnCompleteListener(new OnCompleteListener<GoogleSignInAccount>() {
                @Override
                public void onComplete(Task<GoogleSignInAccount> task) {
                    if (task.isSuccessful()) {
                        _this.log("onActivityResult: Task<GoogleSignInAccount>.getResult");
                        GoogleSignInAccount account = task.getResult();

                        _this.log("onActivityResult: AUTHENTICATED!");
                        _this.signedInAccount = account;
                        _this.obtainCredentials();
                    } else {
                        // FIXME: Currently it fails with code 10 which means “DEVELOPER_ERROR”
                        // Maybe because it’s a non-signed non-authorized application or something?

                        _this.log("FAILURE onActivityResult: Task<GoogleSignInAccount>.getException");
                        // See GoogleSignInStatusCodes for error code explanation.
                        // https://developers.google.com/android/reference/com/google/android/gms/auth/api/signin/GoogleSignInStatusCodes
                        Exception e = task.getException();
                        _this.log("FAILURE: onActivityResult: " + e.getMessage());
                        // ApiException e
                        // _this.log("FAILURE: onActivityResult: failed code=" + e.getStatusCode());
                    }
                }
            });

            this.log("onActivityResult: The flow continues asynchronously");
        }

        this.log("onActivityResult: END");
    }

}
