package com.demo.android.mymoviestest.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MoviesDao {

    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> getAllMovies();

    @Query("SELECT * FROM favourite_movie")
    LiveData<List<FavouriteMovie>> getAllFavouriteMovies();

    @Query("SELECT * FROM search_movie ORDER BY popularity DESC")
    LiveData<List<Movie>> getAllSearchMovies();

    @Query("DELETE FROM movies")
    void deleteAllMovies();

    @Query("DELETE FROM favourite_movie")
    void deleteAllFavouriteMovies();

    @Query("DELETE FROM search_movie")
    void deleteAllSearchingMovies();

    @Insert
    void insertMovie(Movie movie);

    @Insert
    void insertFavouriteMovie(FavouriteMovie favouriteMovie);

    @Insert
    void insertSearchMovie(SearchMovie searchMovie);

    @Delete
    void deleteMovie(Movie movie);

    @Delete
    void deleteFavouriteMovie(FavouriteMovie favouriteMovie);

    @Delete
    void deleteSearchMovie(SearchMovie searchMovie);

    @Query("SELECT * FROM movies WHERE id = :id")
    Movie getMovieById(int id);

    @Query("SELECT * FROM favourite_movie WHERE id = :id")
    FavouriteMovie getFavouriteMovieById(int id);

    @Query("SELECT * FROM search_movie WHERE id = :id")
    SearchMovie getSearchMovieById(int id);
}
