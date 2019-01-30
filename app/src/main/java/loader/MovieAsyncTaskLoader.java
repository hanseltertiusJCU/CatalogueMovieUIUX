package loader;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import com.example.android.cataloguemovieuiux.BuildConfig;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import item.MovieItems;

import cz.msebera.android.httpclient.Header;

public class MovieAsyncTaskLoader extends AsyncTaskLoader<ArrayList<MovieItems>> {

    private String apiKey = BuildConfig.MOVIE_API_KEY;
    private ArrayList<MovieItems> mMovieData;
    private boolean mHasResult = false;
    private String mMovieMode;
    private String mMovieSearch;
    private boolean mNoKeywordMovieSearch;

    public MovieAsyncTaskLoader(Context context, String movieMode) {
        super(context);
        // Ketika isi dari Loader itu berganti, panggil method tsb.
        onContentChanged();
        this.mMovieMode = movieMode;
        this.mNoKeywordMovieSearch = true;
    }

    public MovieAsyncTaskLoader(Context context, String movieMode, String movieSearch) {
        super(context);
        // Ketika isi dari Loader itu berganti, panggil method tsb.
        onContentChanged();
        this.mMovieMode = movieMode;
        this.mMovieSearch = movieSearch;
        this.mNoKeywordMovieSearch = false;
    }

    @Override
    protected void onStartLoading() {
        // takeContentChanged() itu adalah hasil dari panggilan {@link onContentChanged()} dan
        // hasil dari takeContentChanged() method itu adalah true
        if (takeContentChanged())
            // Ketika hasil dari takeContentChanged() itu true, panggil forceLoad() untuk
            // load data ketika data belum ada ataupun ada perubahan
            forceLoad();
        else if (mHasResult)
            deliverResult(mMovieData);
    }

    // Method tersebut gunanya untuk menampilkan result data dan isi2 dari ArrayList<MovieItems>
    // kebawa ke method ini
    @Override
    public void deliverResult(ArrayList<MovieItems> data) {
        mMovieData = data;
        mHasResult = true;
        super.deliverResult(data);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (mHasResult) {
            onReleaseResources(mMovieData);
            mMovieData = null;
            mHasResult = false;
        }
    }

    // Method tsb gunanya untuk mencegah memory leak dengan menghapus memory yang ada.
    private void onReleaseResources(ArrayList<MovieItems> mMovieData) {
        // Do nothing, karena kita sedang tidak butuh menghapus memory
    }

    @Override
    public ArrayList<MovieItems> loadInBackground() {

        // Menginisiasikan SyncHttpClientObject krn Loader itu sudah berjalan pada background thread
        SyncHttpClient syncHttpClient = new SyncHttpClient();

        final ArrayList<MovieItems> movieItemses = new ArrayList<>();

        if(mMovieMode.equals("search")){
            if (mNoKeywordMovieSearch) {
                String searchUrl = "https://api.themoviedb.org/3/search/movie?api_key=" + apiKey + "&query=avenger";

                // Panggil get method untuk melakukan request terhadap web service melalui HTTP GET
                syncHttpClient.get(searchUrl, new AsyncHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        super.onStart();
                        // Membuat proses dari handlerloopj menjadi Synchronous yang semula adalah
                        // Asynchronous
                        setUseSynchronousMode(true);
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            // Convert byte ke string untuk mempermudah pembuatan JSON Object
                            String result = new String(responseBody);
                            JSONObject responseObject = new JSONObject(result);
                            // Dapatkan JSON Array karena datanya itu berada di dalam array
                            JSONArray results = responseObject.getJSONArray("results");

                            // Iterate array untuk mendapatkan sebuah object dari array
                            for (int i = 0; i < results.length(); i++) {
                                JSONObject movie = results.getJSONObject(i);
                                MovieItems movieItems = new MovieItems(movie);
                                movieItemses.add(movieItems);
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
            } else {
                String searchUrl = "https://api.themoviedb.org/3/search/movie?api_key=" + apiKey + "&query=" + mMovieSearch;
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
                                if(!movieItems.getMoviePosterPath().equals("null")){
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
            }
        } else if(mMovieMode.equals("nowPlaying")){
            String nowPlayingUrl = "https://api.themoviedb.org/3/movie/now_playing?api_key=" + apiKey;
            syncHttpClient.get(nowPlayingUrl, new AsyncHttpResponseHandler() {
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
                            if(!movieItems.getMoviePosterPath().equals("null")){
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
        }


        return movieItemses;
    }
}