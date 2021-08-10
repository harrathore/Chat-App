package com.example.mywhastapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.mywhastapp.Adapters.FragmentsAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    ViewPager myviewpager;
    TabLayout mytablayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        // getSupportActionBar().hide();   //getSupportActionBar().hide();
        myviewpager = (ViewPager) findViewById(R.id.viewPager);
        mytablayout = (TabLayout)findViewById(R.id.tablayout);

        myviewpager.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
        mytablayout.setupWithViewPager(myviewpager);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.setting:
                Intent intent3 = new Intent(MainActivity.this,  SettingsActivity.class);
                startActivity(intent3);
                break;
            case R.id.crGroup:
                Intent intent1 = new Intent(MainActivity.this, GroupChatActivity.class);
                startActivity(intent1);
                break;
            case R.id.logout:
                auth.signOut();
                Intent intent2 = new Intent(MainActivity.this, SigninActivity.class);
                startActivity(intent2);
                break;
        }
        return true;
    }
}