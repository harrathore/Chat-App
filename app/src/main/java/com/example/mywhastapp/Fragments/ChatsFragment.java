package com.example.mywhastapp.Fragments;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.mywhastapp.Adapters.UsersAdapter;
import com.example.mywhastapp.Models.Users;
import com.example.mywhastapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ChatsFragment extends Fragment {

    ArrayList<Users> list = new ArrayList<>();
    FirebaseDatabase database;
    RecyclerView recyclerView;

    public ChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chats, container, false);      //Inflating the Fragment View (ChatFragment)

        UsersAdapter adapter = new UsersAdapter(list, getContext());
        recyclerView = rootView.findViewById(R.id.chatrecyclerview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
        database = FirebaseDatabase.getInstance();

        database.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Users user = dataSnapshot.getValue(Users.class);    // Other data Values from firebase
                    user.setUserId(dataSnapshot.getKey());             //Setting UserId of the current user
                    if(!user.getUserId().equals(FirebaseAuth.getInstance().getUid())) {
                        list.add(user);                                     //Adding User to List of Users class type
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return rootView;                                              //retuning te View to Fragments Adapter

    }

}