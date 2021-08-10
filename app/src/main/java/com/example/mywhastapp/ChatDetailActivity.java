package com.example.mywhastapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mywhastapp.Adapters.ChatAdapter;
import com.example.mywhastapp.Models.MessagesModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import static java.util.Date.parse;

public class ChatDetailActivity extends AppCompatActivity {
    public static final String DATE_FORMAT_1 = "hh:mm a";
    FirebaseDatabase database;
    FirebaseAuth auth;
    TextView user_name;
    ImageView profilePicture;
    ImageView backArroy;
    RecyclerView recyclerView;
    ImageView btn_send;
    EditText msg_text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_detail);
        getSupportActionBar().hide();
        database = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();

        user_name = (TextView)findViewById(R.id.textView_un);
        profilePicture = (ImageView)findViewById(R.id.profile_image);
        backArroy = (ImageView)findViewById(R.id.back_arroy);
        btn_send = (ImageView) findViewById(R.id.btn_send_msg);
        msg_text = (EditText) findViewById(R.id.tv_write_msg);

        final String senderId = auth.getUid();                                     //Getting sender Id from auth of Firebase(Current User)
        String recieverId = getIntent().getStringExtra("userId");             //Reciever Id from where Intent is called

        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profilePicture");
        if(userName.length()<21)
            user_name.setText(userName);
        else
            user_name.setText(userName.substring(0, 20));
        Picasso.get().load(profilePic).placeholder(R.drawable.avatar).into(profilePicture);

        final ArrayList<MessagesModel> list = new ArrayList<>();                           // List of Objects of MessageModel type(All msgs)
        final ChatAdapter chatAdapter = new ChatAdapter(list, this, recieverId);   // Adapter Object(list of users, context, recieverId)
        recyclerView = findViewById(R.id.chatRecyclerView);
        recyclerView.setAdapter(chatAdapter);                                             //Setting Adapter on Recycler View
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);



        final String SenderRoom = senderId + recieverId;                                    //Id for Sender Room and reciever Room
        final String RecieverRoom = recieverId +  senderId;                                 //Sender room and reciver room all same mesagges

        database.getReference().child("Chats").child(SenderRoom).addValueEventListener(new ValueEventListener() {  //getting all messages from Chats->SenderRoom
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                 list.clear();
                 for(DataSnapshot dataSnapshot : snapshot.getChildren()){                                  //Adding all messages to list to send to Adapter
                   MessagesModel msgmodel = dataSnapshot.getValue(MessagesModel.class);
                   msgmodel.setMessageId(dataSnapshot.getKey());                                    //Setting Message Id
                   list.add(msgmodel);
                 }
                 chatAdapter.notifyDataSetChanged();                                                //Notifying Adapter that we have added data to you
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = msg_text.getText().toString();
                if(msg.length()>1){

                    MessagesModel message = new MessagesModel(senderId, msg);             // Creating object of MessageModel Class
                    message.setTimeStamp(new Date().getTime());                             //setting time of Msg

                    database.getReference().child("Chats").child(SenderRoom).push().setValue(message)   // Saving into Firebase in Chats-senderroom
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            database.getReference().child("Chats").child(RecieverRoom).push().setValue(message)    //saving msg in recievar room
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            });
                        }
                    });
                    msg_text.setText("");
                }
                else{
                    Toast.makeText(ChatDetailActivity.this, "Enter Message First", Toast.LENGTH_LONG).show();
                }
            }
        });

        backArroy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChatDetailActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }
}