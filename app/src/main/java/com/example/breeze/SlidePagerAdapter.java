package com.example.breeze;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

public class SlidePagerAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_PAGES = 3;
    Context context;

    public SlidePagerAdapter(@NonNull FragmentManager fm, Context context) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        this.context = context;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return ChatsFragment.newInstance();

            case 1:
                return GroupFragment.newInstance();

            case 2:
                return ContactFragment.newInstance();

        }
        return ChatsFragment.newInstance();
    }

    @Override
    public int getCount() {
        return NUM_PAGES;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return position == 0 ? context.getString(R.string.chats) : position == 1 ? context.getString(R.string.groups) : context.getString(R.string.contacts);
    }
}
