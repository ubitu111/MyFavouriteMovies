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
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Switch;
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
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<JSONObject> {
    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private Switch switchSort;
    private int methodOfSort = 0;
    private MainViewModel viewModel;
    private LiveData<List<Movie>> movies;
    private TextView textViewPopular;
    private TextView textViewTopRated;
    private int page = 1;
    private static boolean isLoading = false;
    private Toast exitToast;
    private static final int LOADER_ID = 144;
    private LoaderManager loaderManager;
    private ProgressBar progressBarLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.recycleViewMovies);
        textViewPopular = findViewById(R.id.textViewPopularity);
        textViewTopRated = findViewById(R.id.textViewTopRated);
        progressBarLoading = findViewById(R.id.progressBarLoading);
        loaderManager = LoaderManager.getInstance(this);
        movieAdapter = new MovieAdapter();
        recyclerView.setAdapter(movieAdapter);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        downloadData();
        movies = viewModel.getMovies();
        switchSort = findViewById(R.id.switchSort);
        movieAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                int id = movieAdapter.getMovies().get(position).getId();
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        recyclerView.setLayoutManager(new GridLayoutManager(this, getColumnCount()));


        switchSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                page = 1;
                setMethodOfSort(b);
            }
        });


        movies.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> moviesOnLiveData) {
                if  (page == 1) {
                    movieAdapter.setMovies(moviesOnLiveData);
                }
                /*else {
                    movieAdapter.addMovies(moviesOnLiveData);
                }*/
            }
        });

        movieAdapter.setOnReachEndListener(new OnReachEndListener() {
            @Override
            public void onReachEnd() {
                if (!isLoading) {
                    downloadData();
                }
            }
        });

    }

    private int getColumnCount(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);
        return width / 185 > 2 ? width / 185 : 2;
    }

    private void downloadData(){
        //viewModel.downloadData(methodOfSort, page);

        URL url = NetworkUtil.buildURLForMovies(methodOfSort, page);
        Bundle bundle = new Bundle();
        bundle.putString("url", url.toString());
        loaderManager.restartLoader(LOADER_ID, bundle, this);
    }

    public void onClickChangeMethodOfSort(View view) {
        int idPosition = view.getId();
        if (idPosition == R.id.textViewPopularity) {
            switchSort.setChecked(false);
        }
        else {
            switchSort.setChecked(true);
        }
    }

    private void setMethodOfSort(boolean isTopRated) {
        if (isTopRated) {
            methodOfSort = 1;
            textViewPopular.setTextColor(getResources().getColor(R.color.colorWhite));
            textViewTopRated.setTextColor(getResources().getColor(R.color.colorAccent));
        } else {
            methodOfSort = 0;
            textViewPopular.setTextColor(getResources().getColor(R.color.colorAccent));
            textViewTopRated.setTextColor(getResources().getColor(R.color.colorWhite));
        }
        /*page = 1;*/
        downloadData();
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
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.search_enter_movie_title), Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(MainActivity.this, SearchResultsActivity.class);
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

    @Override
    public void onBackPressed() {
        if (exitToast == null || exitToast.getView() == null || exitToast.getView().getWindowToken() == null) {
            exitToast = Toast.makeText(this, R.string.press_again_to_exit, Toast.LENGTH_LONG);
            exitToast.show();
        } else {
            exitToast.cancel();
            super.onBackPressed();
        }

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
                movieAdapter.clear();
            }
            for (Movie movie : movies) {
                viewModel.insertMovie(movie);
            }
            movieAdapter.addMovies(movies);
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
