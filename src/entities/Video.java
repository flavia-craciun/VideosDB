package entities;

import java.util.ArrayList;

public abstract class Video {
    private String title;
    private int year;
    private String videoType;
    private ArrayList<String> genres;
    private ArrayList<String> cast;


    public final String getTitle() {
        return title;
    }

    public final int getYear() {
        return year;
    }

    public final String getVideoType() {
        return videoType;
    }

    public final ArrayList<String> getGenres() {
        return genres;
    }

    public final ArrayList<String> getCast() {
        return cast;
    }

    public final void setTitle(final String title) {
        this.title = title;
    }

    public final void setYear(final int year) {
        this.year = year;
    }

    public final void setVideoType(final String type) {
        this.videoType = type;
    }

    public final void setGenres(final ArrayList<String> genres) {
        this.genres = genres;
    }

    public final void setCast(final ArrayList<String> cast) {
        this.cast = cast;
    }
}
