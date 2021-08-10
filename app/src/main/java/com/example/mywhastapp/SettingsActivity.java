package com.example.mywhastapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.mywhastapp.Models.Users;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class SettingsActivity extends AppCompatActivity {

    ImageView back, plus, profile;
    EditText name, et_status;
    Button save;

    FirebaseAuth auth;
    FirebaseStorage dataStorage;
    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSupportActionBar().hide();

        final int PICK_IMAGE = 100;
        back = (ImageView) findViewById(R.id.back_setting);
        plus = (ImageView) findViewById(R.id.plus);
        profile = (ImageView)findViewById(R.id.profile_image_gc);
        name = (EditText) findViewById(R.id.update_username);
        et_status = (EditText) findViewById(R.id.update_status);
        save = (Button)findViewById(R.id.btn_save);

        auth = FirebaseAuth.getInstance();
        dataStorage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

         database.getReference().child("Users").child(auth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {   //getting profile from Firebasedatabase
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {                            //Listeners on database gives info as snapshot
                Users user = snapshot.getValue(Users.class);                                       //Getting user from snapshot
                Picasso.get().load(user.getProfilePicture()).placeholder(R.drawable.avatar).into(profile);
                name.setText(user.getUsername());
                et_status.setText(user.getStatus());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileUpdate();
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileUpdate();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uname = name.getText().toString();
                String Status = et_status.getText().toString();
                if(uname.length()<3)
                    Toast.makeText(SettingsActivity.this, "Please enter Name", Toast.LENGTH_SHORT).show();
                else if(Status.length()<3)
                    Toast.makeText(SettingsActivity.this, "Enter Your Status", Toast.LENGTH_LONG).show();
                else{
                    HashMap<String, Object> obj = new HashMap<>();
                    obj.put("username", uname);
                    obj.put("status", Status);
                    database.getReference().child("Users").child(auth.getUid()).updateChildren(obj);
                    Toast.makeText(SettingsActivity.this, "Name Updated", Toast.LENGTH_LONG).show();
                    //Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                    //startActivity(intent);
                }
            }
        });

    }
    private void profileUpdate() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, 33);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {             //Activity for Result
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 33 && data.getData() != null){
            Uri file = data.getData();
            profile.setImageURI(file);

            StorageReference reference = dataStorage.getReference().child("profilePictures").child(auth.getUid());
            reference.putFile(file).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                   reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {      //Getting Url from storage
                       @Override
                       public void onSuccess(Uri uri) {
                          database.getReference().child("Users").child(auth.getUid()).            //saving imageUrl in Databse to set on RecyclerView
                                  child("profilePicture").setValue(uri.toString());
                           Toast.makeText(SettingsActivity.this, "Profile Updated", Toast.LENGTH_LONG).show(); //Updating values in firebase
                       }
                   });
                }
            });
        }
        else{
            Toast.makeText(SettingsActivity.this, "Image is not Selected", Toast.LENGTH_LONG).show();
        }
    }
}