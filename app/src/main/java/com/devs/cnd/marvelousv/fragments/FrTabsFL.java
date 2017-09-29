package com.devs.cnd.marvelousv.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.devs.cnd.marvelousv.R;
import com.devs.cnd.marvelousv.adapters.AdapterFLViewPager;

/**
 * Created by wunder on 9/12/17.
 */

public class FrTabsFL extends Fragment {
    private ViewPager viewPager;
    private AdapterFLViewPager adapterFLViewPager;
    private TabLayout tabLayout;

    public FrTabsFL() {
        // Required empty public constructor
    }

    public static FrTabsFL newInstance() {
        FrTabsFL fragment = new FrTabsFL();
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fr_tabs_mode, container, false);

        viewPager = (ViewPager) rootView.findViewById(R.id.view_pager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout);

        adapterFLViewPager=new AdapterFLViewPager(getChildFragmentManager());
        viewPager.setAdapter(adapterFLViewPager);

        tabLayout.setupWithViewPager(viewPager);
        //tabLayout.getTabAt(0).setText("Personajes");
        //tabLayout.getTabAt(1).setText("Habilidades");
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_contacts_white_24dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_person_add_white_24dp);

        // tabLayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        // tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        return rootView; // super.onCreateView(inflater, container, savedInstanceState);
    }


}
