package com.example.esmaeel.downem;

/**
 * Created by esmaeel on 4/30/2017.
 */

public class VideoData {
    public String title;
    public String videoId;
    public String description;
    public String imageUrl;


    public VideoData(){
    }

    public VideoData(String title,String  videoId,String  description,String  imageUrl) {
        this.videoId = videoId;
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;

    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
