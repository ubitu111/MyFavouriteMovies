package com.demo.android.mymoviestest.data;

import androidx.room.Entity;
import androidx.room.Ignore;

@Entity(tableName = "favourite_movie")
public class FavouriteMovie extends Movie {

    public FavouriteMovie(int uniqueId, String title, String originalTitle, int id, double popularity, int voteCount, double voteAverage, String overview, String releaseDate, String posterPath, String bigPosterPath, String backdropPath) {
        super(uniqueId, title, originalTitle, id, popularity, voteCount, voteAverage, overview, releaseDate, posterPath, bigPosterPath, backdropPath);
    }

    @Ignore
    public FavouriteMovie(String title, String originalTitle, int id, double popularity, int voteCount, double voteAverage, String overview, String releaseDate, String posterPath, String bigPosterPath, String backdropPath) {
        super(title, originalTitle, id, popularity, voteCount, voteAverage, overview, releaseDate, posterPath, bigPosterPath, backdropPath);
    }

    @Ignore
    public FavouriteMovie(Movie movie) {
        super(movie.getTitle(), movie.getOriginalTitle(), movie.getId(), movie.getPopularity(), movie.getVoteCount(), movie.getVoteAverage(), movie.getOverview(), movie.getReleaseDate(), movie.getPosterPath(), movie.getBigPosterPath(), movie.getBackdropPath());
    }
}
