package com.example.android.cataloguemovieuiux;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import adapter.MovieAdapter;
import item.MovieItems;
import loader.MovieAsyncTaskLoader;
import support.MovieItemClickSupport;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<MovieItems>>{

    static final int LOADER_ID_MOVIE = 101;
    // Key untuk membawa data ke intent (data tidak d private untuk dapat diakses ke {@link DetailActivity})
    static final String MOVIE_ID_DATA = "MOVIE_ID_DATA";
    static final String MOVIE_TITLE_DATA = "MOVIE_TITLE_DATA";
    // Key untuk meretrieve search
    private static final String EXTRAS_MOVIE_SEARCH = "EXTRAS_MOVIE_SEARCH";
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private EditText searchEditText;
    private Button searchButton;
    private ProgressBar progressBar;
    private String movieSearch;

    View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            movieSearch = searchEditText.getText().toString();

            // Cek ketika edit textnya itu kosong atau tidak, ketika iya maka program tsb tidak
            // ngapa-ngapain dengan return nothing
            if (TextUtils.isEmpty(movieSearch))
                return;

            // Jika isi dari edit textnya itu tidak kosong, maka kita akan merestart loader untuk
            // mengaccomodate Search
            Bundle bundle = new Bundle();
            bundle.putString(EXTRAS_MOVIE_SEARCH, movieSearch);

            // Ketika kita ngeclick search, maka data akan melakukan loading kembali
            recyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            // Restart loader karena kita sudah membuat loader di onCreate
            getLoaderManager().restartLoader(LOADER_ID_MOVIE, bundle, MainActivity.this);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        movieAdapter = new MovieAdapter(this);
        movieAdapter.notifyDataSetChanged();

        recyclerView = findViewById(R.id.rv_list);

        searchEditText = findViewById(R.id.edit_movie_search);
        searchButton = findViewById(R.id.button_search);

        progressBar = findViewById(R.id.progress_bar);

        // Set visiblity of views ketika sedang dalam meretrieve data
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        searchButton.setOnClickListener(myListener);

        getLoaderManager().initLoader(LOADER_ID_MOVIE, savedInstanceState, this);

    }

    @Override
    public Loader<ArrayList<MovieItems>> onCreateLoader(int id, Bundle args) {
        MovieAsyncTaskLoader movieLoader;

        String movieResult = "";

        if (args != null)
            movieResult = args.getString(EXTRAS_MOVIE_SEARCH);

        // Cek jika movieResult itu tidak ada, jika tidak ada maka kita akan call constructor tanpa
        // ada parameter tambahan
        if (movieResult.isEmpty())
            movieLoader = new MovieAsyncTaskLoader(this);
        else
            movieLoader = new MovieAsyncTaskLoader(this, movieResult);


        return movieLoader;
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<MovieItems>> loader, final ArrayList<MovieItems> movieItems) {
        // Ketika data selesai di load, maka kita akan mendapatkan data dan menghilangkan progress bar
        // yang menandakan bahwa loadingnya sudah selesai
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        movieAdapter.setData(movieItems);
        recyclerView.setAdapter(movieAdapter);
        // Set item click listener di dalam recycler view
        MovieItemClickSupport.addSupportToView(recyclerView).setOnItemClickListener(new MovieItemClickSupport.OnItemClickListener() {
            // Implement interface method
            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View view) {
                // Panggil method showSelectedMovieItems untuk mengakses DetailActivity bedasarkan data yang ada
                showSelectedMovieItems(movieItems.get(position));
            }
        });
    }

    private void showSelectedMovieItems(MovieItems movieItems){
        // Dapatkan id dan title bedasarkan ListView item
        int movieIdItem = movieItems.getId();
        String movieTitleItem = movieItems.getMovieTitle();
        Intent intentWithMovieIdData = new Intent(MainActivity.this, DetailActivity.class);
        // Bawa data untuk disampaikan ke {@link DetailActivity}
        intentWithMovieIdData.putExtra(MOVIE_ID_DATA, movieIdItem);
        intentWithMovieIdData.putExtra(MOVIE_TITLE_DATA, movieTitleItem);
        // Start activity tujuan bedasarkan intent object
        startActivity(intentWithMovieIdData);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<MovieItems>> loader) {
        movieAdapter.setData(null);
    }

}
