package com.example.mywhastapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.mywhastapp.databinding.ActivitySigninBinding;

import com.example.mywhastapp.Models.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    FirebaseDatabase database;
    protected Button btn_signupp;
    EditText etemail;
    EditText etuser;
    EditText etpass;
    TextView alr_acc;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        btn_signupp = (Button)findViewById(R.id.btn_signUp);
        etuser = (EditText)findViewById(R.id.userName);
        etemail = (EditText)findViewById(R.id.etEmail_su);
        etpass = (EditText)findViewById(R.id.etPassword_su);
        alr_acc = (TextView)findViewById(R.id.tv_account_su);
        progressDialog = new ProgressDialog(SignupActivity.this);
        progressDialog.setTitle("Creating User");
        progressDialog.setMessage("We are creating Your account");
    }
    public void onStart(){
        super.onStart();
        btn_signupp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(SignupActivity.this, "SignUp CLicked", Toast.LENGTH_LONG).show();
                progressDialog.show();
                auth.createUserWithEmailAndPassword(etemail.getText().toString(),                             //auth using email pass
                        etpass.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {           //task is result return by call
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            Users user = new Users(etuser.getText().toString(), etemail.getText().toString(),   //Creating new User using class
                                    etpass.getText().toString());
                            String id = task.getResult().getUser().getUid();   //getting current user if from auth results
                            user.setUserId(id);                                //setting user Id
                            database.getReference().child("Users").child(id).setValue(user); //saving in database
                            Toast.makeText(SignupActivity.this, "User Created Successfully", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(SignupActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else{
                            Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });

        alr_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignupActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        });
    }

}
