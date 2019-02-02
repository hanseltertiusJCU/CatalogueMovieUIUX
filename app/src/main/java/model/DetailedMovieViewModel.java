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

import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
import item.DetailedMovieItems;

public class DetailedMovieViewModel extends AndroidViewModel{

    // Gunakan Build Config untuk melindungi credential
    private String apiKey = BuildConfig.MOVIE_API_KEY;
    private String detailedUrlBase = BuildConfig.BASE_MOVIE_DETAILED_URL;
    private String apiKeyFiller = BuildConfig.DETAILED_MOVIE_API_KEY_FILLER;


    private DetailedMovieLiveData detailedMovieLiveData;

    private int mDetailedMovieId;

    public LiveData<ArrayList<DetailedMovieItems>> getDetailedMovie(){
        return detailedMovieLiveData;
    }

    public DetailedMovieViewModel(@NonNull Application application, int detailedMovieId) {
        super(application);
        this.mDetailedMovieId = detailedMovieId;
        // Buat LiveData agar dapat di return ke getDetailedMovie method
        detailedMovieLiveData = new DetailedMovieLiveData(application, detailedMovieId);
    }


    private class DetailedMovieLiveData extends LiveData<ArrayList<DetailedMovieItems>> {
        private final Context context;
        private final int id;

        // Buat constructor untuk mengakomodasi parameter yang ada dari {@link DetailedMovieViewModel}
        public DetailedMovieLiveData(Context context, int id){
            this.context = context;
            this.id = id;
            loadDetailedMovieLiveData();
        }

        @SuppressLint("StaticFieldLeak")
        private void loadDetailedMovieLiveData() {

            new AsyncTask<Void, Void, ArrayList<DetailedMovieItems>>(){

                @Override
                protected ArrayList<DetailedMovieItems> doInBackground(Void... voids) {

                    SyncHttpClient syncHttpClient = new SyncHttpClient();

                    final ArrayList<DetailedMovieItems> detailedMovieItemses = new ArrayList<>();

                    String detailedMovieUrl = detailedUrlBase + mDetailedMovieId + apiKeyFiller + apiKey;

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

                @Override
                protected void onPostExecute(ArrayList<DetailedMovieItems> detailedMovieItems) {
                    setValue(detailedMovieItems);
                }
            }.execute();
        }


    }
}
