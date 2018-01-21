package com.audora.digiroom;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MyQuizzes myQuizzes = new MyQuizzes();
                return myQuizzes;
            case 1:
                MyAssignments myAssignments = new MyAssignments();
                return myAssignments;
            case 2:
                MyNotifications myNotifications = new MyNotifications();
                return myNotifications;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
