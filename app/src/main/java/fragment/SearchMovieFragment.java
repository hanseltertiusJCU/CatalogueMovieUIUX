package fragment;


import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v4.app.LoaderManager;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.Loader;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;


import com.example.android.cataloguemovieuiux.DetailActivity;
import com.example.android.cataloguemovieuiux.R;

import java.util.ArrayList;

import adapter.MovieAdapter;
import factory.SearchViewModelFactory;
import item.MovieItems;
import model.SearchViewModel;
import support.MovieItemClickSupport;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchMovieFragment extends Fragment {


    public static final int LOADER_ID_MOVIE = 101;
    // Key untuk membawa data ke intent (data tidak d private untuk dapat diakses ke {@link DetailActivity})
    public static final String MOVIE_ID_DATA = "MOVIE_ID_DATA";
    public static final String MOVIE_TITLE_DATA = "MOVIE_TITLE_DATA";
    // Key untuk meretrieve search
    private static final String EXTRAS_MOVIE_SEARCH = "EXTRAS_MOVIE_SEARCH";
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private EditText searchEditText;
    private Button searchButton;
    private ProgressBar progressBar;
    private String movieSearch;

    private SearchViewModel searchViewModel;

    public SearchMovieFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_movie, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Assign view yang ada di Fragment
        searchEditText = view.findViewById(R.id.edit_movie_search);
        searchButton = view.findViewById(R.id.button_search);
        progressBar = view.findViewById(R.id.progress_bar);

        movieAdapter = new MovieAdapter(getContext());
        movieAdapter.notifyDataSetChanged();

        recyclerView = view.findViewById(R.id.rv_list);

        // Buat object DividerItemDecoration dan set drawable untuk DividerItemDecoration
        DividerItemDecoration itemDecorator = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        itemDecorator.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.item_divider));

        // Set divider untuk RecyclerView items
        recyclerView.addItemDecoration(itemDecorator);

        searchEditText = view.findViewById(R.id.edit_movie_search);
        searchButton = view.findViewById(R.id.button_search);

        // Set visiblity of views ketika sedang dalam meretrieve data
        recyclerView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.VISIBLE);

        searchButton.setOnClickListener(mySearchListener);


    }

    View.OnClickListener mySearchListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            movieSearch = searchEditText.getText().toString();

            // Cek ketika edit textnya itu kosong atau tidak, ketika iya maka program tsb tidak
            // ngapa-ngapain dengan return nothing
            if (TextUtils.isEmpty(movieSearch))
                return;

            // Ketika kita ngeclick search, maka data akan melakukan loading kembali
            recyclerView.setVisibility(View.INVISIBLE);
            progressBar.setVisibility(View.VISIBLE);

            // todo: Ubah value moviesearch
        }
    };

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        movieSearch = "avenger";

        // Dapatkan ViewModel yang tepat dari ViewModelProviders
        searchViewModel = ViewModelProviders.of(this, new SearchViewModelFactory(getActivity().getApplication(), movieSearch)).get(SearchViewModel.class);

        final Observer<ArrayList<MovieItems>> searchObserver = new Observer<ArrayList<MovieItems>>() {
            @Override
            public void onChanged(@Nullable final ArrayList<MovieItems> movieItems) {
                // Ketika data selesai di load, maka kita akan mendapatkan data dan menghilangkan progress bar
                // yang menandakan bahwa loadingnya sudah selesai
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
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
        };

        // Tempelkan Observer ke LiveData object
        searchViewModel.getSearchMovie().observe(this, searchObserver);
    }

    private void showSelectedMovieItems(MovieItems movieItems){
        // Dapatkan id dan title bedasarkan ListView item
        int movieIdItem = movieItems.getId();
        String movieTitleItem = movieItems.getMovieTitle();
        Intent intentWithMovieIdData = new Intent(getActivity(), DetailActivity.class);
        // Bawa data untuk disampaikan ke {@link DetailActivity}
        intentWithMovieIdData.putExtra(MOVIE_ID_DATA, movieIdItem);
        intentWithMovieIdData.putExtra(MOVIE_TITLE_DATA, movieTitleItem);
        // Start activity tujuan bedasarkan intent object
        startActivity(intentWithMovieIdData);
    }
}
