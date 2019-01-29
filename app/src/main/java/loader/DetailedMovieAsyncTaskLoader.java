package loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.preference.PreferenceActivity;

import com.example.android.cataloguemovieuiux.BuildConfig;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONObject;

import java.util.ArrayList;

import item.DetailedMovieItems;

import cz.msebera.android.httpclient.Header;

public class DetailedMovieAsyncTaskLoader extends AsyncTaskLoader<ArrayList<DetailedMovieItems>> {

    private String apiKey = BuildConfig.MOVIE_API_KEY;
    private ArrayList<DetailedMovieItems> mDetailedMovieData;
    private boolean mHasResult = false;
    private int mDetailedMovieId;

    public DetailedMovieAsyncTaskLoader(Context context, int detailedMovieId) {
        super(context);

        onContentChanged();
        this.mDetailedMovieId = detailedMovieId;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        if (takeContentChanged())
            forceLoad();
        else if (mHasResult)
            deliverResult(mDetailedMovieData);
    }

    @Override
    public void deliverResult(ArrayList<DetailedMovieItems> detailedData) {
        mDetailedMovieData = detailedData;
        mHasResult = true;
        super.deliverResult(detailedData);
    }

    @Override
    protected void onReset() {
        super.onReset();
        onStopLoading();
        if (mHasResult) {
            onReleaseResources(mDetailedMovieData);
            mDetailedMovieData = null;
            mHasResult = false;
        }
    }

    private void onReleaseResources(ArrayList<DetailedMovieItems> mDetailedMovieData) {
    }

    @Override
    public ArrayList<DetailedMovieItems> loadInBackground() {

        SyncHttpClient syncHttpClient = new SyncHttpClient();

        final ArrayList<DetailedMovieItems> detailedMovieItemses = new ArrayList<>();

        String detailedMovieUrl = "https://api.themoviedb.org/3/movie/" + mDetailedMovieId + "?api_key=" + apiKey;

        syncHttpClient.get(detailedMovieUrl, new AsyncHttpResponseHandler() {

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
                    DetailedMovieItems detailedMovieItems = new DetailedMovieItems(responseObject);
                    detailedMovieItemses.add(detailedMovieItems);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

        return detailedMovieItemses;
    }
}