package com.demo.android.mymoviestest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SearchView;
import android.widget.Toast;

/***************************************************************************************************
 * Заменен на SearchView
 **************************************************************************************************/
public class SearchActivity extends AppCompatActivity {
    private EditText editTextMovieTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        editTextMovieTitle = findViewById(R.id.editTextMovieTitle);

    }

    public void onClickSearch(View view) {
        String movieTitle = editTextMovieTitle.getText().toString();
        if (movieTitle.isEmpty()) {
            Toast.makeText(this, "Введите название фильма!", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent intent = new Intent(this, SearchResultsActivity.class);
            intent.putExtra("movieTitle", movieTitle);
            editTextMovieTitle.setText("");
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
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
