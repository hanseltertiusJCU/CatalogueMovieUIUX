package adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.cataloguemovieuiux.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import item.MovieItems;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private ArrayList<MovieItems> mMovieData = new ArrayList<>();
    private Context context;

    public MovieAdapter(Context context) {
        this.context = context;
    }

    public ArrayList<MovieItems> getmMovieData() {
        return mMovieData;
    }

    public void setmMovieData(ArrayList<MovieItems> mMovieData) {
        this.mMovieData = mMovieData;
    }

    public Context getContext() {
        return context;
    }

    public void setData(ArrayList<MovieItems> mData) {
        this.mMovieData = mData;
        // Method tersebut berguna untuk memanggil adapter bahwa ada data yg bru, sehingga data tsb
        // dpt ditampilkan pada ListView yg berisi adapter yg berkaitan dengan ListView
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        // Set layout xml yang berisi movie items ke View
        View movieItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_items, viewGroup, false);
        // Return MovieViewHolder dengan memanggil constructor MovieViewHolder yang berisi View sbg
        // parameter
        return new MovieViewHolder(movieItem);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder movieViewHolder, int position) {
        // Load image jika ada poster path
        Picasso.get().load("https://image.tmdb.org/t/p/w185" + mMovieData.get(position).getMoviePosterPath()).into(movieViewHolder.imageViewMoviePoster);

        movieViewHolder.textViewMovieTitle.setText(mMovieData.get(position).getMovieTitle());

        // Set textview content in movie item rating to contain a variety of different colors
        Spannable ratingMovieItemWord = new SpannableString(context.getString(R.string.span_movie_item_ratings));
        ratingMovieItemWord.setSpan(new ForegroundColorSpan(Color.BLACK), 0, ratingMovieItemWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        movieViewHolder.textViewMovieRatings.setText(ratingMovieItemWord);
        Spannable ratingMovieItem = new SpannableString(mMovieData.get(position).getMovieRatings());
        ratingMovieItem.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.colorAccent)), 0, ratingMovieItem.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        movieViewHolder.textViewMovieRatings.append(ratingMovieItem);

        // Set textview content in movie item release date to contain a variety of different colors
        Spannable releaseDateMovieItemWord = new SpannableString(context.getString(R.string.span_movie_item_release_date));
        releaseDateMovieItemWord.setSpan(new ForegroundColorSpan(Color.BLACK), 0, releaseDateMovieItemWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        movieViewHolder.textViewMovieReleaseDate.setText(releaseDateMovieItemWord);
        Spannable releaseDateMovieItem = new SpannableString(mMovieData.get(position).getMovieReleaseDate());
        releaseDateMovieItem.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.colorAccent)), 0, releaseDateMovieItem.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        movieViewHolder.textViewMovieReleaseDate.append(releaseDateMovieItem);

        // Set textview content in movie item original language to contain a variety of different colors
        Spannable languageMovieItemWord = new SpannableString(context.getString(R.string.span_movie_item_language));
        languageMovieItemWord.setSpan(new ForegroundColorSpan(Color.BLACK), 0, languageMovieItemWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        movieViewHolder.textViewMovieOriginalLanguage.setText(languageMovieItemWord);
        Spannable languageMovieItem = new SpannableString(mMovieData.get(position).getMovieLanguage());
        languageMovieItem.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.colorAccent)), 0, languageMovieItem.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        movieViewHolder.textViewMovieOriginalLanguage.append(languageMovieItem);
    }

    @Override
    public long getItemId(int position) {
        // Return position dari sebuah item di RecyclerView
        return position;
    }

    @Override
    public int getItemCount() {
        return getmMovieData().size();
    }

    // Kelas ini berguna untuk menampung view yang ada tanpa mendeclare view di sebuah Adapter
    public class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewMoviePoster;
        TextView textViewMovieTitle;
        TextView textViewMovieRatings;
        TextView textViewMovieReleaseDate;
        TextView textViewMovieOriginalLanguage;

        // Assign view di dalam constructor
        public MovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewMoviePoster = itemView.findViewById(R.id.poster_image);
            textViewMovieTitle = itemView.findViewById(R.id.movie_title_text);
            textViewMovieRatings = itemView.findViewById(R.id.movie_ratings_text);
            textViewMovieReleaseDate = itemView.findViewById(R.id.movie_release_date_text);
            textViewMovieOriginalLanguage = itemView.findViewById(R.id.movie_language_text);
        }
    }

}