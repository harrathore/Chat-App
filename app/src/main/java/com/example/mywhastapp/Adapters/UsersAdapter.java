package com.example.mywhastapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mywhastapp.ChatDetailActivity;
import com.example.mywhastapp.Models.Users;
import com.example.mywhastapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.viewHolder>{

    ArrayList<Users> list;
    Context context;
    public UsersAdapter(ArrayList<Users> list, Context context)
    {
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sampl_show_user, parent, false);     //Inflating View
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Users user = list.get(position);                                               // Current User from list using position
        Picasso.get().load(user.getProfilePicture()).placeholder(R.drawable.avatar).into(holder.profileimg);    //setting profile picture from firebase  holder.profileimg.setImageResource(user.getProfilePicture());
        holder.username.setText(user.getUsername());                                   //setting username using Users class

        FirebaseDatabase.getInstance().getReference().child("Chats").                    //setting the last message
                child(FirebaseAuth.getInstance().getUid() + user.getUserId()).
                orderByChild("timeStamp").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChildren()) {
                    for(DataSnapshot snapshot1 : snapshot.getChildren()){
                        String lstmsg = snapshot1.child("message").getValue().toString();
                        holder.lastmsg.setText(lstmsg);   //setting time for last mesage
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatDetailActivity.class);
                intent.putExtra("userId", user.getUserId());                      //Getting user Id(Primary Key - From Firebase)
                intent.putExtra("profilePicture", user.getProfilePicture());
                intent.putExtra("userName", user.getUsername());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder{                       // Creating a Single View
        ImageView profileimg;
        TextView username, lastmsg;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            profileimg = itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.usernamelist);
            lastmsg = itemView.findViewById(R.id.lastMsg);
        }
    }
}
