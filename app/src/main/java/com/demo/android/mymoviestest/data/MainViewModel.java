package com.demo.android.mymoviestest.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.demo.android.mymoviestest.util.JSONUtil;
import com.demo.android.mymoviestest.util.NetworkUtil;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainViewModel extends AndroidViewModel {
    private static MoviesDatabase database;
    private LiveData<List<Movie>> movies;

    public MainViewModel(@NonNull Application application) {
        super(application);
        database = MoviesDatabase.getInstance(getApplication());
        movies = database.moviesDao().getAllMovies();
    }

    public void downloadData(int methodOfSort, int page) {

        List<Movie> listOfMovies = JSONUtil.getMovies(NetworkUtil.getJSONObjectForMovies(methodOfSort, page));
        if (listOfMovies != null && listOfMovies.size() > 0) {
            if (page == 1) {
                deleteAllMovies();
            }
            for (Movie movie : listOfMovies) {
                insertMovie(movie);
            }
        }
    }

    public boolean searchMovies(String query, int page) {
        List<Movie> movies = JSONUtil.getMovies(NetworkUtil.getJSONObjectFromSearch(query, page));
        if (movies != null && movies.size() > 0) {
            if (page == 1) {
                deleteAllSearchMovie();
            }
            for (Movie movie : movies) {
                insertSearchMovie(new SearchMovie(movie));
            }
            return true;
        }
        return false;
    }

    public LiveData<List<Movie>> getSearchMovies() {
        return database.moviesDao().getAllSearchMovies();
    }

    public LiveData<List<Movie>> getMovies() {
        return movies;
    }

    public void insertMovie (Movie movie) {
        new InsertTask().execute(movie);
    }

    public void deleteMovie (Movie movie) {
        new DeleteTask().execute(movie);
    }

    public void deleteAllMovies() {
        new DeleteAllTask().execute();
    }

    public Movie getMovieById(int id) {
        try {
            Movie movie = new GetMovieTask().execute(id).get();
            return movie;
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public LiveData<List<FavouriteMovie>> getFavouriteMovies(){
        return database.moviesDao().getAllFavouriteMovies();
    }

    public void insertFavouriteMovie(FavouriteMovie movie) {
        new InsertFavouriteMovieTask().execute(movie);
    }

    public void deleteAllFavouriteMovies(){
        new DeleteAllFavouriteMovieTask().execute();
    }

    public void deleteAllSearchMovie(){
        new DeleteAllSearchMovieTask().execute();
    }

    public void deleteFavouriteMovie(FavouriteMovie movie) {
        new DeleteFavouriteMovieTask().execute(movie);
    }

    public FavouriteMovie getFavouriteMovieById(int id) {
        try {
            return new GetFavouriteMovieTask().execute(id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertSearchMovie(SearchMovie searchMovie){
        new InsertSearchMovieTask().execute(searchMovie);
    }

    public SearchMovie getSearchMovieById(int id){
        try {
            return new GetSearchMovieTask().execute(id).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static class InsertTask extends AsyncTask<Movie, Void, Void> {

        @Override
        protected Void doInBackground(Movie... movies) {
            if (movies != null && movies.length > 0) {
                database.moviesDao().insertMovie(movies[0]);
            }
            return null;
        }
    }

    private static class DeleteTask extends AsyncTask<Movie, Void, Void> {

        @Override
        protected Void doInBackground(Movie... movies) {
            if (movies != null && movies.length > 0) {
                database.moviesDao().deleteMovie(movies[0]);
            }
            return null;
        }
    }

    private static class DeleteAllTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            database.moviesDao().deleteAllMovies();
            return null;
        }
    }

    private static class GetMovieTask extends AsyncTask<Integer, Void, Movie> {

        @Override
        protected Movie doInBackground(Integer... integers) {
            if (integers != null && integers.length > 0) {
                return database.moviesDao().getMovieById(integers[0]);
            }
            return null;
        }
    }

    private static class InsertFavouriteMovieTask extends AsyncTask<FavouriteMovie, Void, Void> {

        @Override
        protected Void doInBackground(FavouriteMovie... movies) {
            if (movies != null && movies.length > 0) {
                database.moviesDao().insertFavouriteMovie(movies[0]);
            }
            return null;
        }
    }

    private static class InsertSearchMovieTask extends AsyncTask<SearchMovie, Void, Void> {

        @Override
        protected Void doInBackground(SearchMovie... movies) {
            if (movies != null && movies.length > 0) {
                database.moviesDao().insertSearchMovie(movies[0]);
            }
            return null;
        }
    }

    private static class DeleteAllFavouriteMovieTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            database.moviesDao().deleteAllFavouriteMovies();
            return null;
        }
    }

    private static class DeleteAllSearchMovieTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            database.moviesDao().deleteAllSearchingMovies();
            return null;
        }
    }

    private static class DeleteFavouriteMovieTask extends AsyncTask<FavouriteMovie, Void, Void> {

        @Override
        protected Void doInBackground(FavouriteMovie... movies) {
            if (movies != null && movies.length > 0) {
                database.moviesDao().deleteFavouriteMovie(movies[0]);
            }
            return null;
        }
    }

    private static class GetFavouriteMovieTask extends AsyncTask<Integer, Void, FavouriteMovie> {

        @Override
        protected FavouriteMovie doInBackground(Integer... integers) {
            if (integers != null && integers.length > 0) {
                return database.moviesDao().getFavouriteMovieById(integers[0]);
            }
            return null;
        }
    }

    private static class GetSearchMovieTask extends AsyncTask<Integer, Void, SearchMovie> {

        @Override
        protected SearchMovie doInBackground(Integer... integers) {
            if (integers != null && integers.length > 0) {
                return database.moviesDao().getSearchMovieById(integers[0]);
            }
            return null;
        }
    }
}
