package com.example.android.cataloguemovieuiux;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import adapter.MovieSectionsFragmentPagerAdapter;
import fragment.NowPlayingMovieFragment;
import fragment.SearchMovieFragment;
import fragment.UpcomingMovieFragment;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private MovieSectionsFragmentPagerAdapter movieSectionsFragmentPagerAdapter;

    private TextView tabNowPlaying;
    private TextView tabUpcoming;
    private TextView tabSearch;

    private Drawable[] nowPlayingDrawables;
    private Drawable nowPlayingDrawable;
    private Drawable[] upcomingDrawables;
    private Drawable upcomingDrawable;
    private Drawable[] searchDrawables;
    private Drawable searchDrawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Set content activity to use layout xml file activity_main.xml
        setContentView(R.layout.activity_main);

        // Set default action bar title, yaitu "Now Playing"
        setActionBarTitle(getString(R.string.now_playing));

        // Create ViewPager untuk swipe Fragments
        viewPager = (ViewPager) findViewById(R.id.movie_viewPager);

        createViewPagerContent(viewPager);

        // Assign TabLayout
        tabLayout = (TabLayout) findViewById(R.id.menu_tabs);
        // Beri ViewPager ke TabLayout
        tabLayout.setupWithViewPager(viewPager);

        createTabIcons();

        // Set listener untuk tab layout
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            // Set action bar title ketika sebuah tab dipilih
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                setActionBarTitle((String) movieSectionsFragmentPagerAdapter.getPageTitle(position));
                // Ubah text color dan drawable tint menjadi colorAccent, yang menandakan bahwa itemnya
                // sedang dipilih
                switch (position){
                    case 0:
                        tabNowPlaying.setTextColor(getResources().getColor(R.color.colorAccent));
                        nowPlayingDrawables = tabNowPlaying.getCompoundDrawables();
                        nowPlayingDrawable = nowPlayingDrawables[1];
                        nowPlayingDrawable.setTint(getResources().getColor(R.color.colorAccent));
                        break;
                    case 1:
                        tabUpcoming.setTextColor(getResources().getColor(R.color.colorAccent));
                        upcomingDrawables = tabUpcoming.getCompoundDrawables();
                        upcomingDrawable = upcomingDrawables[1];
                        upcomingDrawable.setTint(getResources().getColor(R.color.colorAccent));
                        break;
                    case 2:
                        tabSearch.setTextColor(getResources().getColor(R.color.colorAccent));
                        searchDrawables = tabSearch.getCompoundDrawables();
                        searchDrawable = searchDrawables[1];
                        searchDrawable.setTint(getResources().getColor(R.color.colorAccent));
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                // Ubah text color dan drawable tint menjadi hitam, yang menandakan bahwa itemnya
                // sedang tidak dipilih
                switch (position){
                    case 0:
                        tabNowPlaying.setTextColor(getResources().getColor(R.color.color_black));
                        nowPlayingDrawables = tabNowPlaying.getCompoundDrawables();
                        nowPlayingDrawable = nowPlayingDrawables[1];
                        nowPlayingDrawable.setTint(getResources().getColor(R.color.color_black));
                        break;
                    case 1:
                        tabUpcoming.setTextColor(getResources().getColor(R.color.color_black));
                        upcomingDrawables = tabUpcoming.getCompoundDrawables();
                        upcomingDrawable = upcomingDrawables[1];
                        upcomingDrawable.setTint(getResources().getColor(R.color.color_black));
                        break;
                    case 2:
                        tabSearch.setTextColor(getResources().getColor(R.color.color_black));
                        searchDrawables = tabSearch.getCompoundDrawables();
                        searchDrawable = searchDrawables[1];
                        searchDrawable.setTint(getResources().getColor(R.color.color_black));
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    // Method tsb berguna untuk membuat icons beserta isinya di Tab
    private void createTabIcons(){
        tabNowPlaying = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabNowPlaying.setText(getString(R.string.now_playing));
        // Set default text color yang menandakan bahwa tabnya itu sedang d select
        tabNowPlaying.setTextColor(getResources().getColor(R.color.colorAccent));
        // Set icon di atas text
        tabNowPlaying.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_now_playing, 0, 0);
        // Dapatkan getCompoundDrawable dari setCompoundDrawablesWithIntrinsicBounds
        nowPlayingDrawables = tabNowPlaying.getCompoundDrawables();
        // Akses drawableTop, which is in this case kita mengakses element ke 2 (index value: 1)
        // dari Drawable[]
        nowPlayingDrawable = nowPlayingDrawables[1];
        // Set default tint untuk drawable yang menandakan bahwa tabnya itu sedang d select
        nowPlayingDrawable.setTint(getResources().getColor(R.color.colorAccent));

        // Inflate custom_tab.xml ke dalam TabLayout
        tabLayout.getTabAt(0).setCustomView(tabNowPlaying);

        tabUpcoming = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabUpcoming.setText(getString(R.string.upcoming));
        tabUpcoming.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_upcoming, 0,0);
        tabLayout.getTabAt(1).setCustomView(tabUpcoming);

        tabSearch = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabSearch.setText(getString(R.string.search_movie));
        tabSearch.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_search, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabSearch);
    }

    // Method tsb berguna untuk membuat isi dari ViewPager
    private void createViewPagerContent(ViewPager viewPager){

        // Create FragmentPagerAdapter untuk mengetahui fragment mana yg di show
        movieSectionsFragmentPagerAdapter = new MovieSectionsFragmentPagerAdapter(this, getSupportFragmentManager());

        // Tambahkan fragment beserta title ke FragmentPagerAdapter
        movieSectionsFragmentPagerAdapter.addMovieSectionFragment(new NowPlayingMovieFragment(), getString(R.string.now_playing));
        movieSectionsFragmentPagerAdapter.addMovieSectionFragment(new UpcomingMovieFragment(), getString(R.string.upcoming));
        movieSectionsFragmentPagerAdapter.addMovieSectionFragment(new SearchMovieFragment(), getString(R.string.search_movie));

        // Set FragmentPagerAdapter ke ViewPager
        viewPager.setAdapter(movieSectionsFragmentPagerAdapter);
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
