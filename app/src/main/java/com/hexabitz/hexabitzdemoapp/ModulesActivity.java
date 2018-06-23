package com.hexabitz.hexabitzdemoapp;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabLayout;


public class ModulesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragments to adapter one by one
        adapter.addFragment(new H01R00Fragment(), "FRAG1FRAG1FRAG1");
        adapter.addFragment(new H01R00Fragment(), "FRAG2FRAG1FRAG1");
        adapter.addFragment(new H01R00Fragment(), "FRAG3FRAG1FRAG1");
        adapter.addFragment(new H01R00Fragment(), "FRAG4");
        adapter.addFragment(new H01R00Fragment(), "FRAG4");
        adapter.addFragment(new H01R00Fragment(), "FRAG4");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }
}
