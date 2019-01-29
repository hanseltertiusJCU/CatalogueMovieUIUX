package com.example.android.cataloguemovieuiux;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import fragment.MovieFragment;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Buat object FragmentManager
        FragmentManager fragmentManager = getSupportFragmentManager();
        // Buat object FragmentTransaction dengan panggil method beginTransaction
        // dari FragmentManager
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        MovieFragment movieFragment = new MovieFragment();

        Fragment fragment = fragmentManager.findFragmentByTag(MovieFragment.class.getSimpleName());
        // Buat fragment klo fragmentnya tidak exist di MovieFragment
        if(!(fragment instanceof MovieFragment)){
            fragmentTransaction.add(R.id.movie_frame_container, movieFragment, MovieFragment.class.getSimpleName());
            Log.d("Fragment in Movie", "Fragment Name :" + MovieFragment.class.getSimpleName());
            fragmentTransaction.commit();
        }

    }

}
