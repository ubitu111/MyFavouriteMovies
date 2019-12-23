package com.demo.android.mymoviestest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.android.mymoviestest.R;
import com.demo.android.mymoviestest.data.FavouriteMovie;
import com.demo.android.mymoviestest.listeners.OnItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FavouriteMovieAdapter extends RecyclerView.Adapter<FavouriteMovieAdapter.FavouriteMovieViewHolder>{
    private List<FavouriteMovie> movies;
    private OnItemClickListener onItemClickListener;


    public FavouriteMovieAdapter() {
        movies = new ArrayList<>();
    }

    @NonNull
    @Override
    public FavouriteMovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourite_movie_item, parent, false);
        return new FavouriteMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteMovieAdapter.FavouriteMovieViewHolder holder, int position) {
        FavouriteMovie movie = movies.get(position);
        if (movie != null) {
            Picasso.get().load(movie.getPosterPath()).placeholder(R.drawable.no_poster).into(holder.imageViewFavouriteSmallPoster);
        }

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    class FavouriteMovieViewHolder extends RecyclerView.ViewHolder{
        private ImageView imageViewFavouriteSmallPoster;

        public FavouriteMovieViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewFavouriteSmallPoster = itemView.findViewById(R.id.imageViewFavouriteSmallPoster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(getAdapterPosition());
                    }
                }
            });

        }
    }

    public List<FavouriteMovie> getMovies() {
        return movies;
    }

    public void setMovies(List<FavouriteMovie> movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }

    public void addMovies(List<FavouriteMovie> movies) {
        this.movies.addAll(movies);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
