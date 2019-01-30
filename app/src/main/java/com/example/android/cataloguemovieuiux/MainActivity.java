package com.example.android.cataloguemovieuiux;

import android.content.Intent;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import adapter.MovieSectionsFragmentPagerAdapter;
import fragment.NowPlayingMovieFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content activity to use layout xml file activity_main.xml
        setContentView(R.layout.activity_main);

        // Buat String array untuk menampung koleksi dari Movie Section Title
        final String[] mTitleNames = {getString(R.string.now_playing), getString(R.string.upcoming), getString(R.string.search_movie)};

        // Set default action bar title
        setActionBarTitle(mTitleNames[0]);

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

        // Set listener untuk tab layout
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            // Set action bar title ketika sebuah tab dipilih
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                setActionBarTitle(mTitleNames[position]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_language_settings, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_change_language_settings){
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void setActionBarTitle(String title){
        // Gunakan getSupportActionBar untuk backward compatibility
        getSupportActionBar().setTitle(title);
    }

}
