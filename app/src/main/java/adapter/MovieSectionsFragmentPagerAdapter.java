package adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.android.cataloguemovieuiux.R;

import fragment.NowPlayingMovieFragment;
import fragment.SearchMovieFragment;

public class MovieSectionsFragmentPagerAdapter extends FragmentPagerAdapter {
    private Context mContext;

    public MovieSectionsFragmentPagerAdapter(Context context, FragmentManager fragmentManager){
        super(fragmentManager);
        mContext = context;
    }

    // Method ini menentukan posisi fragment untuk setiap tab

    @Override
    public Fragment getItem(int position) {
        if(position == 0){
            return new NowPlayingMovieFragment();
        } else {
            return new SearchMovieFragment();
        }
    }

    // Method ini menentukan berapa banyak tabs yang ada
    @Override
    public int getCount() {
        return 2;
    }

    // Method ini berguna untuk mereturn tab title
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return mContext.getString(R.string.now_playing);
            case 1:
                return mContext.getString(R.string.search_movie);
            default:
                return null;
        }
    }
}
