package com.demo.android.mymoviestest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.android.mymoviestest.adapters.VideosAdapter;
import com.demo.android.mymoviestest.data.FavouriteMovie;
import com.demo.android.mymoviestest.data.MainViewModel;
import com.demo.android.mymoviestest.data.Movie;
import com.demo.android.mymoviestest.data.Video;
import com.demo.android.mymoviestest.listeners.OnItemClickListener;
import com.demo.android.mymoviestest.util.JSONUtil;
import com.demo.android.mymoviestest.util.NetworkUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

public class DetailActivity extends AppCompatActivity {
    private TextView textViewTitle;
    private TextView textViewOriginalTitle;
    private TextView textViewPopularity;
    private TextView textViewVoteAverage;
    private TextView textViewReleaseDate;
    private TextView textViewOverview;
    private ImageView imageViewBigPoster;
    private ImageView imageViewFavourite;
    private MainViewModel viewModel;
    private Movie movie;
    private FavouriteMovie favouriteMovie;
    private int id;
    private RecyclerView recyclerViewVideos;
    private VideosAdapter videosAdapter;
    private List<Video> videos;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        textViewTitle = findViewById(R.id.textViewTitle);
        textViewOriginalTitle = findViewById(R.id.textViewOriginalTitle);
        textViewPopularity = findViewById(R.id.textViewPopularity);
        textViewVoteAverage = findViewById(R.id.textViewVoteAverage);
        textViewReleaseDate = findViewById(R.id.textViewReleaseDate);
        textViewOverview = findViewById(R.id.textViewOverview);
        imageViewBigPoster = findViewById(R.id.imageViewBigPoster);
        imageViewFavourite = findViewById(R.id.imageViewFavourite);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        recyclerViewVideos = findViewById(R.id.recyclerViewVideos);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        movie = viewModel.getMovieById(id);
        if (movie == null) {
            movie = viewModel.getFavouriteMovieById(id);
            if (movie == null) {
                movie = viewModel.getSearchMovieById(id);
            }
        }

        videosAdapter = new VideosAdapter();
        videos = JSONUtil.getVideos(NetworkUtil.getJSONObjectForVideos(id));
        videosAdapter.setVideos(videos);
        recyclerViewVideos.setAdapter(videosAdapter);
        recyclerViewVideos.setLayoutManager(new LinearLayoutManager(this));

        videosAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                String url = videos.get(position).getVideoUrl();
                Intent intentYouTube = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intentYouTube);
            }
        });

        setMovieInfo();
        checkFavourite();



    }

    private void setMovieInfo() {
        Picasso.get().load(movie.getBigPosterPath()).placeholder(R.drawable.no_poster).into(imageViewBigPoster);
        textViewTitle.setText(movie.getTitle());
        textViewOriginalTitle.setText(movie.getOriginalTitle());
        textViewPopularity.setText(Double.toString(movie.getPopularity()));
        textViewVoteAverage.setText(Double.toString(movie.getVoteAverage()));
        textViewReleaseDate.setText(movie.getReleaseDate());
        textViewOverview.setText(movie.getOverview());
    }


    public void onClickSetFavourite(View view) {
        if (favouriteMovie == null) {
            viewModel.insertFavouriteMovie(new FavouriteMovie(movie));
            Toast.makeText(this, R.string.add_to_favourite, Toast.LENGTH_SHORT).show();
        }
        else {
            viewModel.deleteFavouriteMovie(favouriteMovie);
            Toast.makeText(this, R.string.remove_from_favourite, Toast.LENGTH_SHORT).show();
        }
        checkFavourite();
    }

    private void checkFavourite(){
       favouriteMovie = viewModel.getFavouriteMovieById(id);
       if (favouriteMovie == null) {
           imageViewFavourite.setImageResource(R.drawable.favourite_no);
       }
       else {
           imageViewFavourite.setImageResource(R.drawable.favourite);
       }
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
                    Toast.makeText(DetailActivity.this, getResources().getString(R.string.search_enter_movie_title), Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(DetailActivity.this, SearchResultsActivity.class);
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
