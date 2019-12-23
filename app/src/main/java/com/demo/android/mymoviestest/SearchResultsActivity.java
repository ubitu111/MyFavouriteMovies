package com.demo.android.mymoviestest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.android.mymoviestest.adapters.MovieAdapter;
import com.demo.android.mymoviestest.data.MainViewModel;
import com.demo.android.mymoviestest.data.Movie;
import com.demo.android.mymoviestest.listeners.OnItemClickListener;
import com.demo.android.mymoviestest.listeners.OnReachEndListener;
import com.demo.android.mymoviestest.listeners.OnStartLoadingListener;
import com.demo.android.mymoviestest.util.JSONUtil;
import com.demo.android.mymoviestest.util.NetworkUtil;

import org.json.JSONObject;

import java.net.URL;
import java.util.List;

public class SearchResultsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject>{
    private MainViewModel viewModel;
    private LiveData<List<Movie>> movies;
    private MovieAdapter adapter;
    private RecyclerView recyclerView;
    private int page = 1;
    private TextView textViewSearchResult;
    private static boolean isLoading = false;
    private static final int LOADER_ID = 149;
    private LoaderManager loaderManager;
    private ProgressBar progressBarLoading;
    private String movieTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        recyclerView = findViewById(R.id.recycleViewSearchResult);
        adapter = new MovieAdapter();
        textViewSearchResult = findViewById(R.id.textViewSearchResult);
        progressBarLoading = findViewById(R.id.progressBarSearching);
        loaderManager = LoaderManager.getInstance(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, getColumnCount()));
        recyclerView.setAdapter(adapter);
        Intent intent = getIntent();
        movieTitle = intent.getStringExtra("movieTitle");
        textViewSearchResult.setText(movieTitle);
        boolean isFind = viewModel.searchMovies(movieTitle, page);

        movies = viewModel.getSearchMovies();

        movies.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> moviesOnLiveData) {
                if (page == 1) {
                    adapter.setMovies(moviesOnLiveData);
                }

            }
        });


        adapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(SearchResultsActivity.this, DetailActivity.class);
                int id = adapter.getMovies().get(position).getId();
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        adapter.setOnReachEndListener(new OnReachEndListener() {
            @Override
            public void onReachEnd() {
                if (!isLoading) {
                    downloadData();
                }
            }
        });

        if (!isFind) {
            viewModel.deleteAllSearchMovie();
            Toast.makeText(this, R.string.search_nothing, Toast.LENGTH_SHORT).show();
        }

    }

    private int getColumnCount(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);
        return width / 185 > 2 ? width / 185 : 2;
    }

    private void downloadData(){
        //viewModel.downloadData(methodOfSort, page);

        URL url = NetworkUtil.buildURLForSearch(movieTitle, page);
        Bundle bundle = new Bundle();
        bundle.putString("url", url.toString());
        loaderManager.restartLoader(LOADER_ID, bundle, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        MenuItem search_item = menu.findItem(R.id.search_view);
        SearchView searchView = (SearchView) search_item.getActionView();
        searchView.setFocusable(false);
        searchView.setQueryHint(getResources().getString(R.string.search_searching));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.isEmpty()) {
                    Toast.makeText(SearchResultsActivity.this, getResources().getString(R.string.search_enter_movie_title), Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(SearchResultsActivity.this, SearchResultsActivity.class);
                    intent.putExtra("movieTitle", s);
                    startActivity(intent);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.itemMain:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.itemFavourite:
                Intent intentToFavourite = new Intent(this, FavouriteActivity.class);
                startActivity(intentToFavourite);
                break;
            /*case R.id.itemSearch:
                Intent intentToSearch = new Intent(this, SearchActivity.class);
                startActivity(intentToSearch);
                break;*/
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle args) {
        NetworkUtil.JSONLoader jsonLoader = new NetworkUtil.JSONLoader(this, args);
        jsonLoader.setOnStartLoadingListener(new OnStartLoadingListener() {
            @Override
            public void onStartLoading() {
                progressBarLoading.setVisibility(View.VISIBLE);
                isLoading = true;
            }
        });
        return jsonLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject data) {
        List<Movie> movies = JSONUtil.getMovies(data);
        if (movies != null && !movies.isEmpty()) {
            if (page == 1) {
                viewModel.deleteAllMovies();
                adapter.clear();
            }
            for (Movie movie : movies) {
                viewModel.insertMovie(movie);
            }
            adapter.addMovies(movies);
            page++;
        }
        isLoading = false;
        progressBarLoading.setVisibility(View.INVISIBLE);
        loaderManager.destroyLoader(LOADER_ID);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {

    }
}
