package com.demo.android.mymoviestest.data;

public class Video {
    private String videoUrl;
    private String videoDescription;

    public Video(String videoUrl, String videoDescription) {
        this.videoUrl = videoUrl;
        this.videoDescription = videoDescription;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getVideoDescription() {
        return videoDescription;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
    }
}
