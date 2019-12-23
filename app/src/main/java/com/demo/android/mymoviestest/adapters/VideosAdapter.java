package com.demo.android.mymoviestest.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.demo.android.mymoviestest.R;
import com.demo.android.mymoviestest.data.Video;
import com.demo.android.mymoviestest.listeners.OnItemClickListener;

import java.util.List;

public class VideosAdapter extends RecyclerView.Adapter<VideosAdapter.VideosViewHolder> {
    private List<Video> videos;
    private OnItemClickListener onItemClickListener;

    @NonNull
    @Override
    public VideosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.videos_item, parent, false);
        return new VideosViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideosViewHolder holder, int position) {
        Video video = videos.get(position);
        holder.textViewVideoDescription.setText(video.getVideoDescription());
    }

    @Override
    public int getItemCount() {
        return videos.size();
    }

    public class VideosViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewVideoDescription;
        public VideosViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewVideoDescription = itemView.findViewById(R.id.textViewVideoDescription);
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

    public void setVideos(List<Video> videos) {
        this.videos = videos;
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}
