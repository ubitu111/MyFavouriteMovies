package com.demo.android.mymoviestest.util;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import com.demo.android.mymoviestest.listeners.OnStartLoadingListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class NetworkUtil {
    private static final String API_KEY = "d4d175044a4874d8ce354b3eadf5073c";

    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    private static final String BASE_URL_SEARCH = "https://api.themoviedb.org/3/search/movie";
    private static final String BASE_URL_VIDEOS = "https://api.themoviedb.org/3/movie/%s/videos";


    private static final String PARAMS_ADULT = "include_adult";
    private static final String PARAMS_LANGUAGE = "language";
    private static final String PARAMS_PAGE = "page";
    private static final String PARAM_SORT_BY = "sort_by";
    private static final String PARAMS_API = "api_key";
    private static final String PARAMS_VOTE_COUNT = "vote_count.gte";
    private static final String PARAMS_QUERY = "query";

    private static final String SORT_BY_POPULARITY = "popularity.desc";
    private static final String SORT_BY_TOP_RATED = "vote_average.desc";
    private static final String VOTE_COUNT = "1000";
    /*private static final String ADULT_TRUE = "true";
    private static final String ADULT_FALSE = "false";*/

    public static URL buildURLForMovies(int methodOfSort, int page) {
        String language = Locale.getDefault().getLanguage();
        URL url = null;
        String sortMethod;
        if (methodOfSort == 0) {
            sortMethod = SORT_BY_POPULARITY;
        } else {
            sortMethod = SORT_BY_TOP_RATED;
        }

        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAMS_API, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, language)
                .appendQueryParameter(PARAM_SORT_BY, sortMethod)
                .appendQueryParameter(PARAMS_PAGE, Integer.toString(page))
                .appendQueryParameter(PARAMS_VOTE_COUNT, VOTE_COUNT)
                .build();

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    public static URL buildURLForSearch(String query, int page) {
        URL url = null;
        String language = Locale.getDefault().getLanguage();

        Uri uri = Uri.parse(BASE_URL_SEARCH).buildUpon()
                .appendQueryParameter(PARAMS_API, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, language)
                .appendQueryParameter(PARAMS_QUERY, query)
                .appendQueryParameter(PARAMS_PAGE, Integer.toString(page))
                //.appendQueryParameter(PARAMS_ADULT, ADULT_TRUE)
                .build();

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static URL buildURLForVideos(int movieId){
        URL url = null;
        String language = Locale.getDefault().getLanguage();
        Uri uri = Uri.parse(String.format(BASE_URL_VIDEOS, movieId)).buildUpon()
                .appendQueryParameter(PARAMS_API, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, language)
                .build();

        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }

    public static JSONObject getJSONObjectForVideos(int movieId){
        JSONObject jsonObject = null;

        try {
            jsonObject = new GetJSONTask().execute(buildURLForVideos(movieId)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static JSONObject getJSONObjectForMovies(int methodOfSort, int page) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new GetJSONTask().execute(buildURLForMovies(methodOfSort,page)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject getJSONObjectFromSearch(String query, int page){
        JSONObject jsonObject = null;
        try {
            jsonObject = new GetJSONTask().execute(buildURLForSearch(query,page)).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static class JSONLoader extends AsyncTaskLoader<JSONObject>{
        private Bundle bundle;
        private OnStartLoadingListener onStartLoadingListener;

        public void setOnStartLoadingListener(OnStartLoadingListener onStartLoadingListener) {
            this.onStartLoadingListener = onStartLoadingListener;
        }

        public JSONLoader(@NonNull Context context, Bundle bundle) {
            super(context);
            this.bundle = bundle;
        }

        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            if (onStartLoadingListener != null) {
                onStartLoadingListener.onStartLoading();
            }
            forceLoad();
        }

        @Nullable
        @Override
        public JSONObject loadInBackground() {
            URL url = null;
            String urlAsString = bundle.getString("url");
            try {
                url = new URL(urlAsString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            JSONObject jsonObject = null;
            HttpURLConnection connection = null;
            StringBuilder builder = new StringBuilder();
            if (url == null){
                return null;
            }
            try {
                connection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = reader.readLine();
                }
                jsonObject = new JSONObject(builder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return jsonObject;
        }
    }

    private static class GetJSONTask extends AsyncTask<URL, Void, JSONObject> {

        @Override
        protected JSONObject doInBackground(URL... urls) {
            JSONObject jsonObject = null;
            HttpURLConnection connection = null;
            StringBuilder builder = new StringBuilder();
            if (urls == null || urls.length == 0){
                return null;
            }
            try {
                connection = (HttpURLConnection) urls[0].openConnection();
                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = reader.readLine();
                while (line != null) {
                    builder.append(line);
                    line = reader.readLine();
                }
                jsonObject = new JSONObject(builder.toString());
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return jsonObject;
        }
    }

}


