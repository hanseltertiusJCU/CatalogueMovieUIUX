package adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.cataloguemovieuiux.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import item.MovieItems;

public class MovieAdapter extends BaseAdapter {

    private ArrayList<MovieItems> mMovieData = new ArrayList<>();
    private LayoutInflater mLayoutInflater;
    private Context context;

    public MovieAdapter(Context context) {
        this.context = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public ArrayList<MovieItems> getmMovieData() {
        return mMovieData;
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

    @Override
    public int getCount() {
        if (mMovieData == null)
            return 0;
        // Kalo ada item(s) di ArrayList, return brp item(s) yang ada
        return mMovieData.size();
    }

    @Override
    public MovieItems getItem(int position) {
        // Return sebuah item dari ArrayList bedasarkan index posisi dari sebuah ArrayList<>
        return mMovieData.get(position);
    }


    @Override
    public long getItemId(int position) {
        // Return position dari sebuah item di ListView
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            // Kita set root menjadi null karena kita ingin mengatur list item, bukan Activity
            convertView = mLayoutInflater.inflate(R.layout.movie_items, null);

            viewHolder.imageViewMoviePoster = convertView.findViewById(R.id.poster_image);
            viewHolder.textViewMovieTitle = convertView.findViewById(R.id.movie_title_text);
            viewHolder.textViewMovieRatings = convertView.findViewById(R.id.movie_ratings_text);
            viewHolder.textViewMovieReleaseDate = convertView.findViewById(R.id.movie_release_date_text);
            viewHolder.textViewMovieOriginalLanguage = convertView.findViewById(R.id.movie_language_text);
            // panggil method setTag untuk membuat memori dari sebuah view
            convertView.setTag(viewHolder);
        } else {
            // panggil method getTag untuk mendapat memori dari sebuah view
            viewHolder = (ViewHolder) convertView.getTag();
        }

        // Load image jika ada poster path
        Picasso.get().load("https://image.tmdb.org/t/p/w185" + mMovieData.get(position).getMoviePosterPath()).into(viewHolder.imageViewMoviePoster);

        viewHolder.textViewMovieTitle.setText(mMovieData.get(position).getMovieTitle());

        // Set textview content in movie item rating to contain a variety of different colors
        Spannable ratingMovieItemWord = new SpannableString("Ratings : ");
        ratingMovieItemWord.setSpan(new ForegroundColorSpan(Color.BLACK), 0, ratingMovieItemWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.textViewMovieRatings.setText(ratingMovieItemWord);
        Spannable ratingMovieItem = new SpannableString(mMovieData.get(position).getMovieRatings());
        ratingMovieItem.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.colorAccent)), 0, ratingMovieItem.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.textViewMovieRatings.append(ratingMovieItem);

        // Set textview content in movie item release date to contain a variety of different colors
        Spannable releaseDateMovieItemWord = new SpannableString("Release Date : ");
        releaseDateMovieItemWord.setSpan(new ForegroundColorSpan(Color.BLACK), 0, releaseDateMovieItemWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.textViewMovieReleaseDate.setText(releaseDateMovieItemWord);
        Spannable releaseDateMovieItem = new SpannableString(mMovieData.get(position).getMovieReleaseDate());
        releaseDateMovieItem.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.colorAccent)), 0, releaseDateMovieItem.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.textViewMovieReleaseDate.append(releaseDateMovieItem);

        // Set textview content in movie item original language to contain a variety of different colors
        Spannable languageMovieItemWord = new SpannableString("Language : ");
        languageMovieItemWord.setSpan(new ForegroundColorSpan(Color.BLACK), 0, languageMovieItemWord.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.textViewMovieOriginalLanguage.setText(languageMovieItemWord);
        Spannable languageMovieItem = new SpannableString(mMovieData.get(position).getMovieLanguage());
        languageMovieItem.setSpan(new ForegroundColorSpan(getContext().getResources().getColor(R.color.colorAccent)), 0, languageMovieItem.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        viewHolder.textViewMovieOriginalLanguage.append(languageMovieItem);

        return convertView;
    }

    // Kelas ini berguna untuk menampung view yang ada tanpa mendeclare view di sebuah Adapter
    private static class ViewHolder {
        ImageView imageViewMoviePoster;
        TextView textViewMovieTitle;
        TextView textViewMovieRatings;
        TextView textViewMovieReleaseDate;
        TextView textViewMovieOriginalLanguage;
    }

}