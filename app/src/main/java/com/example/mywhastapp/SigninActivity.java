package com.example.mywhastapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mywhastapp.Models.Users;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class SigninActivity extends AppCompatActivity {

    EditText email;
    EditText password;
    Button btn_sign;
    TextView tv_create_acc;
    Button btn_gsign;
    ProgressDialog progressDialog;
    FirebaseDatabase database;
    FirebaseAuth auth;

    GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        getSupportActionBar().hide();
        email = (EditText) findViewById(R.id.etEmail_si);
        password = (EditText) findViewById(R.id.etPassword_si);
        btn_sign = (Button) findViewById(R.id.btn_signIn);
        tv_create_acc = (TextView)findViewById(R.id.tv_account_si);
        btn_gsign = (Button)findViewById(R.id.btn_gSignIn);
        progressDialog = new ProgressDialog(SigninActivity.this);
        progressDialog.setTitle("Login");
        progressDialog.setMessage("Login in Your Account");

        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();                                                         //Firebase Auth Object initialization

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)     //To login with Google
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
    }
    public void onStart(){
        super.onStart();
        if(auth.getCurrentUser() != null){                                                          //If User is already loged in current device
            Intent intent = new Intent(SigninActivity.this, MainActivity.class);
            startActivity(intent);
        }
        btn_sign.setOnClickListener(new View.OnClickListener() {                                       //Button Sign Click
            @Override
            public void onClick(View v) {
               progressDialog.show();
               auth.signInWithEmailAndPassword(email.getText().toString(),
                       password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                   @Override
                   public void onComplete(@NonNull Task<AuthResult> task) {
                       progressDialog.dismiss();
                       if(task.isSuccessful()){
                         Toast.makeText(SigninActivity.this, "You Have Login", Toast.LENGTH_LONG).show();
                         Intent intent = new Intent(SigninActivity.this, MainActivity.class);
                         startActivity(intent);
                      }
                       else{
                          Toast.makeText(SigninActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                     }
                   }
               });
            }
        });

        btn_gsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               signIn();
            }
        });

        tv_create_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SigninActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });
    }
    int RC_SIGN_IN = 65;
    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d("TAG", "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w("TAG", "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();              // user is from firebaseuser
                            Users users = new Users();                              // users is creating a object of users class
                            users.setUserId(user.getUid());                          // setting values of users
                            users.setUsername(user.getDisplayName());
                            users.setProfilePicture(user.getPhotoUrl().toString());
                            users.setEmail(user.getEmail());
                            database.getReference().child("Users").child(user.getUid()).setValue(users);  //Saving data to Firebase
                            Intent intent = new Intent(SigninActivity.this,MainActivity.class);
                            startActivity(intent);
                            Toast.makeText(SigninActivity.this, "Sign In with Google", Toast.LENGTH_LONG).show();
                           // updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            //updateUI(null);
                        }
                    }
                });
    }
}