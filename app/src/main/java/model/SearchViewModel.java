package model;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.example.android.cataloguemovieuiux.BuildConfig;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import item.MovieItems;

public class SearchViewModel extends AndroidViewModel {

    // Gunakan beberapa informasi dari Build Config untuk melindungi credential
    private String apiKey = BuildConfig.MOVIE_API_KEY;
    private String searchUrlBase = BuildConfig.BASE_MOVIE_SEARCH_URL;
    private String movieSearchQuery = BuildConfig.MOVIE_SEARCH_QUERY;

    private SearchLiveData searchLiveData;
    private String mMovieSearch;

    public SearchViewModel(@NonNull Application application, String movieSearch) {
        super(application);
        this.mMovieSearch = movieSearch;
        searchLiveData = new SearchLiveData(application, movieSearch);
    }

    public LiveData<ArrayList<MovieItems>> getSearchMovie() {
        return searchLiveData;
    }


    public void setmMovieSearch(String mMovieSearch) {
        this.mMovieSearch = mMovieSearch;
    }

    public void recall() {
        // Panggil live data dengan search keyword yang baru
        searchLiveData = new SearchLiveData(getApplication(), mMovieSearch);
    }

    private class SearchLiveData extends LiveData<ArrayList<MovieItems>> {

        private final Context context;
        private String searchKeyword;

        // Buat constructor untuk mengakomodasi parameter yang ada dari {@link SearchViewModel}

        public SearchLiveData(Context context, String searchKeyword) {
            this.context = context;
            this.searchKeyword = searchKeyword;
            loadSearchMovieLiveData();
        }

        @SuppressLint("StaticFieldLeak")
        private void loadSearchMovieLiveData() {

            new AsyncTask<Void, Void, ArrayList<MovieItems>>() {

                @Override
                protected ArrayList<MovieItems> doInBackground(Void... voids) {

                    // Menginisiasikan SyncHttpClientObject krn Loader itu sudah berjalan pada background thread
                    SyncHttpClient syncHttpClient = new SyncHttpClient();

                    final ArrayList<MovieItems> movieItemses = new ArrayList<>();

                    String searchUrl = searchUrlBase + apiKey + movieSearchQuery + mMovieSearch;
                    syncHttpClient.get(searchUrl, new AsyncHttpResponseHandler() {

                        @Override
                        public void onStart() {
                            super.onStart();
                            setUseSynchronousMode(true);
                        }

                        @Override
                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                            try {
                                String result = new String(responseBody);
                                JSONObject responseObject = new JSONObject(result);
                                JSONArray results = responseObject.getJSONArray("results");
                                // Iterate semua data yg ada dan tambahkan ke ArrayList
                                for (int i = 0; i < results.length(); i++) {
                                    JSONObject movie = results.getJSONObject(i);
                                    MovieItems movieItems = new MovieItems(movie);
                                    // Cek jika posterPath itu tidak "null" karena null dr JSON itu berupa
                                    // String, sehingga perlu menggunakan "" di dalam null
                                    if (!movieItems.getMoviePosterPath().equals("null")) {
                                        movieItemses.add(movieItems);
                                    }

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                            // Do nothing jika responsenya itu tidak berhasil
                        }
                    });

                    return movieItemses;
                }

                @Override
                protected void onPostExecute(ArrayList<MovieItems> movieItems) {
                    // Set value dari Observer yang berisi ArrayList yang merupakan
                    // hasil dari doInBackground method
                    setValue(movieItems);
                }
            }.execute(); // Execute AsyncTask
        }
    }
}
