package com.example.rcjoshi.chanakyafoundation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by RC Joshi on 12-09-2017.
 */

public class Pager extends FragmentStatePagerAdapter {
    int tabCount;
    public Pager(FragmentManager fm, int tabCount) {
        super(fm);
        //initialising tab count
        this.tabCount= tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                AdminSetCourse tab1 = new AdminSetCourse();
                return tab1;
            case 1:
                AdminSetTest tab2 = new AdminSetTest();
                return tab2;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
