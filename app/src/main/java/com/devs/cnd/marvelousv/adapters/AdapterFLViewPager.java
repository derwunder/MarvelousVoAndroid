package com.devs.cnd.marvelousv.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.devs.cnd.marvelousv.aplication.MyApp;
import com.devs.cnd.marvelousv.fragments.FrFriendList;
import com.devs.cnd.marvelousv.fragments.FrFriendRqList;

import static java.security.AccessController.getContext;

/**
 * Created by wunder on 9/12/17.
 */

public class AdapterFLViewPager extends FragmentStatePagerAdapter {



    public AdapterFLViewPager(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment=null;
        FrFriendList frFriendList;
        FrFriendRqList frFriendRqList;
        switch (position){
            case 0: fragment = FrFriendList.newInstance();
                frFriendList =(FrFriendList)fragment;
                frFriendList.getInstance(frFriendList);
                break;
            case 1: fragment = FrFriendRqList.newInstance();
                frFriendRqList =(FrFriendRqList) fragment;
                frFriendRqList.getInstance(frFriendRqList);
                break;

        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
