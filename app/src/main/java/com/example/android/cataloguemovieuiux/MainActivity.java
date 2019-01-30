package com.example.android.cataloguemovieuiux;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import adapter.MovieSectionsFragmentPagerAdapter;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content activity to use layout xml file activity_main.xml
        setContentView(R.layout.activity_main);

        // Create ViewPager untuk swipe Fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.movie_viewPager);

        // Create FragmentPagerAdapter untuk mengetahui fragment mana yg di show
        MovieSectionsFragmentPagerAdapter movieSectionsFragmentPagerAdapter = new
                MovieSectionsFragmentPagerAdapter(this, getSupportFragmentManager());

        // Set FragmentPagerAdapter ke ViewPager
        viewPager.setAdapter(movieSectionsFragmentPagerAdapter);

        // Assign TabLayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.menu_tabs);
        // Beri ViewPager ke TabLayout
        tabLayout.setupWithViewPager(viewPager);

    }

}
