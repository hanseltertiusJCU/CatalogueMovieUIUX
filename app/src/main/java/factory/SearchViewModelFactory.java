package factory;

import android.app.Application;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import model.SearchViewModel;

public class SearchViewModelFactory implements ViewModelProvider.Factory {

    private Application mApplication;
    private String mSearchKeyword;

    public SearchViewModelFactory(Application application, String searchKeyword) {
        mApplication = application;
        mSearchKeyword = searchKeyword;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SearchViewModel(mApplication, mSearchKeyword);
    }
}
