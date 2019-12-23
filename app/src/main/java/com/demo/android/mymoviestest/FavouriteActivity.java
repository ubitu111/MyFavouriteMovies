package com.demo.android.mymoviestest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import com.demo.android.mymoviestest.adapters.FavouriteMovieAdapter;
import com.demo.android.mymoviestest.data.FavouriteMovie;
import com.demo.android.mymoviestest.data.MainViewModel;
import com.demo.android.mymoviestest.listeners.OnItemClickListener;

import java.util.List;

public class FavouriteActivity extends AppCompatActivity {
    private RecyclerView recyclerViewFavourite;
    private FavouriteMovieAdapter favouriteMovieAdapter;
    private LiveData<List<FavouriteMovie>> movies;
    private MainViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        recyclerViewFavourite = findViewById(R.id.recycleViewFavouriteMovies);
        favouriteMovieAdapter = new FavouriteMovieAdapter();
        recyclerViewFavourite.setLayoutManager(new GridLayoutManager(this, getColumnCount()));
        recyclerViewFavourite.setAdapter(favouriteMovieAdapter);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        movies = viewModel.getFavouriteMovies();

        favouriteMovieAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent(FavouriteActivity.this, DetailActivity.class);
                int id = favouriteMovieAdapter.getMovies().get(position).getId();
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        movies.observe(this, new Observer<List<FavouriteMovie>>() {
            @Override
            public void onChanged(List<FavouriteMovie> moviesOnLiveData) {
                    favouriteMovieAdapter.setMovies(moviesOnLiveData);
            }
        });


    }

    private int getColumnCount(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = (int) (displayMetrics.widthPixels / displayMetrics.density);
        return width / 185 > 2 ? width / 185 : 2;
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
                    Toast.makeText(FavouriteActivity.this, getResources().getString(R.string.search_enter_movie_title), Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(FavouriteActivity.this, SearchResultsActivity.class);
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
}
