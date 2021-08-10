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

import java.util.ArrayList;
import java.util.Date;

public class GroupChatActivity extends AppCompatActivity {

    ImageView backArroy, profileImage, sendmsg;
    TextView Username;
    EditText msgtxt;
    RecyclerView recyclerView;

    FirebaseDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);
        getSupportActionBar().hide();

        backArroy = (ImageView) findViewById(R.id.back_arroy_gc);
        recyclerView  = (RecyclerView)findViewById(R.id.grpchatRecyclerView);
        Username = (TextView)findViewById(R.id.textView_gc_un);
        sendmsg = (ImageView)findViewById(R.id.btn_send_msg_gc);
        msgtxt = (EditText)findViewById(R.id.tv_write_msg_gc);
        database = FirebaseDatabase.getInstance();

        final ArrayList<MessagesModel> list = new ArrayList<>();
        final ChatAdapter adapter = new ChatAdapter(list, this);               // Adapter of ChatActivity

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setAdapter(adapter);                                             //Setting Adapter on Recycler View
        recyclerView.setLayoutManager(linearLayoutManager);

        final  String senderId = FirebaseAuth.getInstance().getUid();
        Username.setText("My Group");


        database.getReference().child("Group Chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot data : snapshot.getChildren()){
                    MessagesModel message = data.getValue(MessagesModel.class);
                    list.add(message);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        sendmsg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  String message = msgtxt.getText().toString();
                if(message.length()>0){
                    final MessagesModel model = new MessagesModel(senderId, message, new Date().getTime());
                    database.getReference().child("Group Chat").push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                        }
                    });
                }
                else{
                    Toast.makeText(GroupChatActivity.this, "Enter Message First", Toast.LENGTH_LONG).show();
                }
                msgtxt.setText("");
            }
        });

        backArroy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GroupChatActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}