package com.demo.android.mymoviestest.util;

import com.demo.android.mymoviestest.data.Movie;
import com.demo.android.mymoviestest.data.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class JSONUtil {

    private static final String PARAM_RESULTS = "results";

    private static final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/";
    private static final String BASE_URL_VIDEOS = "https://www.youtube.com/watch?v=";

    private static final String PARAM_ID = "id";
    private static final String PARAM_VOTE_COUNT = "vote_count";
    private static final String PARAM_VOTE_AVERAGE = "vote_average";
    private static final String PARAM_POPULARITY = "popularity";
    private static final String PARAM_TITLE = "title";
    private static final String PARAM_ORIGINAL_TITLE = "original_title";
    private static final String PARAM_OVERVIEW = "overview";
    private static final String PARAM_RELEASE_DATE = "release_date";
    private static final String PARAM_POSTER_PATH = "poster_path";
    private static final String PARAM_BACKDROP_PATH = "backdrop_path";
    private static final String PARAM_KEY_VIDEO = "key";
    private static final String PARAM_NAME_VIDEO = "name";

    private static final String PARAM_SMALL_POSTER_SIZE = "w185";
    private static final String PARAM_BIG_POSTER_SIZE = "w780";

    private static List<Movie> movies;
    private static List<Video> videos;

    public static List<Movie> getMovies(JSONObject jsonObject) {
        fillListMovies(jsonObject);
        return movies;
    }

    private static void fillListMovies(JSONObject jsonObject) {
        movies = new ArrayList<>();
        try {
            if (jsonObject != null) {
            JSONArray jsonArray = jsonObject.getJSONArray(PARAM_RESULTS);

            if (jsonArray.length() > 0) {
                for (int i = 0 ; i < jsonArray.length(); i++) {
                    JSONObject movieJSON = jsonArray.getJSONObject(i);
                    int id = Integer.parseInt(movieJSON.getString(PARAM_ID));
                    int voteCount = Integer.parseInt(movieJSON.getString(PARAM_VOTE_COUNT));
                    double voteAverage = Double.parseDouble(movieJSON.getString(PARAM_VOTE_AVERAGE));
                    double popularity = Double.parseDouble(movieJSON.getString(PARAM_POPULARITY));
                    String title = movieJSON.getString(PARAM_TITLE);
                    String originalTitle = movieJSON.getString(PARAM_ORIGINAL_TITLE);
                    String overview = movieJSON.getString(PARAM_OVERVIEW);
                    String releaseDate = movieJSON.getString(PARAM_RELEASE_DATE);
                    String posterPath = BASE_POSTER_URL + PARAM_SMALL_POSTER_SIZE + movieJSON.getString(PARAM_POSTER_PATH);
                    String bigPosterPath = BASE_POSTER_URL + PARAM_BIG_POSTER_SIZE + movieJSON.getString(PARAM_POSTER_PATH);
                    String backdropPath = BASE_POSTER_URL + PARAM_SMALL_POSTER_SIZE + movieJSON.getString(PARAM_BACKDROP_PATH);

                    Movie movie = new Movie(title, originalTitle, id, popularity, voteCount, voteAverage, overview, releaseDate, posterPath, bigPosterPath, backdropPath);
                    movies.add(movie);
                }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static List<Video> getVideos(JSONObject jsonObjectVideos) {
        videos = new ArrayList<>();

        try {
            if (jsonObjectVideos != null) {
                JSONArray jsonArray = jsonObjectVideos.getJSONArray(PARAM_RESULTS);

                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        String videoUrl = BASE_URL_VIDEOS + object.getString(PARAM_KEY_VIDEO);
                        String videoDescription = object.getString(PARAM_NAME_VIDEO);
                        videos.add(new Video(videoUrl, videoDescription));
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return videos;
    }

}
