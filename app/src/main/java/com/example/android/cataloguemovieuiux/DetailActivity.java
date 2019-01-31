package com.example.android.cataloguemovieuiux;

import android.app.LoaderManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Loader;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import factory.DetailedMovieViewModelFactory;
import fragment.SearchMovieFragment;
import item.DetailedMovieItems;
import loader.DetailedMovieAsyncTaskLoader;
import model.DetailedMovieViewModel;
import model.NowPlayingViewModel;

public class DetailActivity extends AppCompatActivity {

    static final int LOADER_ID_SEARCH = 102;
    private ImageView imageViewDetailedPosterImage;
    private TextView textViewDetailedMovieTitle;
    private TextView textViewDetailedMovieTagline;
    private TextView textViewDetailedMovieRuntime;
    private TextView textViewDetailedMovieRating;
    private TextView textViewDetailedMovieLanguage;
    private TextView textViewDetailedMovieGenres;
    private TextView textViewDetailedMovieReleaseDate;
    private TextView textViewDetailedMovieOverview;
    private int detailedMovieId;
    private String detailedMovieTitle;
    private LinearLayout detailedMovieContentItem;
    private ProgressBar detailedProgressBar;

    private DetailedMovieViewModel detailedMovieViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        // Setup views bedasarkan id yang ada di layout xml
        imageViewDetailedPosterImage = findViewById(R.id.detailed_poster_image);
        textViewDetailedMovieTitle = findViewById(R.id.detailed_movie_title_text);
        textViewDetailedMovieTagline = findViewById(R.id.detailed_movie_tagline_text);
        textViewDetailedMovieRuntime = findViewById(R.id.detailed_movie_status_text);
        textViewDetailedMovieRating = findViewById(R.id.detailed_movie_rating_text);
        textViewDetailedMovieLanguage = findViewById(R.id.detailed_movie_languages_text);
        textViewDetailedMovieGenres = findViewById(R.id.detailed_movie_genres_text);
        textViewDetailedMovieReleaseDate = findViewById(R.id.detailed_movie_release_date_text);
        textViewDetailedMovieOverview = findViewById(R.id.detailed_movie_overview_text);

        // Get intent untuk mendapatkan id dan title dari {@link MainActivity}
        detailedMovieId = getIntent().getIntExtra(SearchMovieFragment.MOVIE_ID_DATA, 0);
        detailedMovieTitle = getIntent().getStringExtra(SearchMovieFragment.MOVIE_TITLE_DATA);

        // Set layout value untuk dapat menjalankan process loading data
        detailedMovieContentItem = findViewById(R.id.detailed_movie_item);
        detailedProgressBar = findViewById(R.id.detailed_progress_bar);

        // Set title untuk DetailActivity
        setTitle(detailedMovieTitle);

        // Set visiblity of views ketika sedang dalam meretrieve data
        detailedMovieContentItem.setVisibility(View.INVISIBLE);
        detailedProgressBar.setVisibility(View.VISIBLE);

        // Panggil MovieViewModel dengan menggunakan ViewModelFactory sebagai parameter tambahan (dan satu-satunya pilihan) selain activity
        detailedMovieViewModel = ViewModelProviders.of(this, new DetailedMovieViewModelFactory(this.getApplication(), detailedMovieId)).get(DetailedMovieViewModel.class);

        final Observer<ArrayList<DetailedMovieItems>> detailedMovieObserver = new Observer<ArrayList<DetailedMovieItems>>() {
            @Override
            public void onChanged(@Nullable ArrayList<DetailedMovieItems> detailedMovieItems) {
                // Ketika data selesai di load, maka kita akan mendapatkan data dan menghilangkan progress bar
                // yang menandakan bahwa loadingnya sudah selesai
                detailedMovieContentItem.setVisibility(View.VISIBLE);
                detailedProgressBar.setVisibility(View.GONE);

                // Set semua data ke dalam detail activity
                // Load image jika ada poster path
                Picasso.get().load("https://image.tmdb.org/t/p/w185" + detailedMovieItems.get(0).getDetailedMoviePosterPath()).into(imageViewDetailedPosterImage);

                textViewDetailedMovieTitle.setText(detailedMovieItems.get(0).getDetailedMovieTitle());

                textViewDetailedMovieTagline.setText("\"" + detailedMovieItems.get(0).getDetailedMovieTagline() + "\"");

                // Set textview content in detailed movie runtime to contain a variety of different colors
                Spannable statusWord = new SpannableString(getString(R.string.span_movie_detail_status));
                statusWord.setSpan(new ForegroundColorSpan(Color.BLACK), 0, statusWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textViewDetailedMovieRuntime.setText(statusWord);
                Spannable statusDetailedMovie = new SpannableString(detailedMovieItems.get(0).getDetailedMovieStatus());
                statusDetailedMovie.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), 0, statusDetailedMovie.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textViewDetailedMovieRuntime.append(statusDetailedMovie);

                // Set textview content in detailed movie rating to contain a variety of different colors
                Spannable ratingWord = new SpannableString(getString(R.string.span_movie_detail_rating));
                ratingWord.setSpan(new ForegroundColorSpan(Color.BLACK), 0, ratingWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textViewDetailedMovieRating.setText(ratingWord);
                Spannable ratingDetailedMovie = new SpannableString(detailedMovieItems.get(0).getDetailedMovieRatings());
                ratingDetailedMovie.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), 0, ratingDetailedMovie.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textViewDetailedMovieRating.append(ratingDetailedMovie);

                Spannable ratingFromWord = new SpannableString(getString(R.string.span_movie_detail_from));
                ratingFromWord.setSpan(new ForegroundColorSpan(Color.BLACK), 0, ratingFromWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textViewDetailedMovieRating.append(ratingFromWord);

                Spannable ratingDetailedMovieVotes = new SpannableString(detailedMovieItems.get(0).getDetailedMovieRatingsVote());
                ratingDetailedMovieVotes.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorAccent)), 0, ratingDetailedMovieVotes.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textViewDetailedMovieRating.append(ratingDetailedMovieVotes);

                Spannable ratingVotesWord = new SpannableString(getString(R.string.span_movie_detail_votes));
                ratingVotesWord.setSpan(new ForegroundColorSpan(Color.BLACK), 0, ratingVotesWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                textViewDetailedMovieRating.append(ratingVotesWord);

                textViewDetailedMovieLanguage.setText(detailedMovieItems.get(0).getDetailedMovieLanguages());

                textViewDetailedMovieGenres.setText(detailedMovieItems.get(0).getDetailedMovieGenres());

                textViewDetailedMovieReleaseDate.setText(detailedMovieItems.get(0).getDetailedMovieReleaseDate());

                textViewDetailedMovieOverview.setText(detailedMovieItems.get(0).getDetailtedMovieOverview());
            }
        };

        // Tempelkan Observer ke LiveData object
        detailedMovieViewModel.getDetailedMovie().observe(this, detailedMovieObserver);
    }
}

