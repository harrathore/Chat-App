package com.example.mywhastapp.Adapters;

import com.example.mywhastapp.Fragments.CallsFragment;
import com.example.mywhastapp.Fragments.ChatsFragment;
import com.example.mywhastapp.Fragments.StatusFragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class FragmentsAdapter extends FragmentPagerAdapter {

    public FragmentsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ChatsFragment();
                //break;
            case 1:
                return new StatusFragment();
                //break;
            case 2:
                return new CallsFragment();
                //break;
            default:
                return new ChatsFragment();
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title = null;
        if(position==0)
            title = "Chats";
        else if(position==1)
            title = "Status";
        else
            title = "Calls";
        return title;
        //return super.getPageTitle(position);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
