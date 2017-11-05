package com.devs.cnd.marvelousv.aplication;

import android.app.Application;
import android.content.Context;

import com.devs.cnd.marvelousv.firebase.FriendsRF;
import com.devs.cnd.marvelousv.firebase.UserDataRF;
import com.devs.cnd.marvelousv.firebase.Wordboxes;
import com.devs.cnd.marvelousv.firebase.WordboxesGF;
import com.devs.cnd.marvelousv.fragments.FrWordboxes;

/**
 * Created by wunder on 7/16/17.
 */

public class MyApp extends Application {

    private static MyApp sInstance;
    public Wordboxes wordboxes;
    public WordboxesGF wordboxesGF;
    public FriendsRF friendsRF;
    public UserDataRF userDataRF;



    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        wordboxes= new Wordboxes(getApplicationContext());
        wordboxesGF= new WordboxesGF(getApplicationContext());
        friendsRF= new FriendsRF(getApplicationContext());
        userDataRF= new UserDataRF(getApplicationContext());
    }



}
