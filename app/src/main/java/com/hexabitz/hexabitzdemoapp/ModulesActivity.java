package com.hexabitz.hexabitzdemoapp;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabLayout;

import com.hexabitz.hexabitzdemoapp.tabs_fragments.BtnsAndSwitchesFragment;
import com.hexabitz.hexabitzdemoapp.tabs_fragments.CLIFragment;
import com.hexabitz.hexabitzdemoapp.tabs_fragments.H01R00Fragment;
import com.hexabitz.hexabitzdemoapp.tabs_fragments.H0FR60Fragment;


public class ModulesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modules);

        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // Add Fragments to adapter one by one
        adapter.addFragment(new H01R00Fragment(), "H01R00");
        adapter.addFragment(new H0FR60Fragment(), "H0FR60");
        adapter.addFragment(new BtnsAndSwitchesFragment(), "Buttons & Switches");
        adapter.addFragment(new CLIFragment(), "CLI");
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


    }
}
