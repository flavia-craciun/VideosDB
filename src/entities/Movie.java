package entities;

import common.Constants;
import fileio.MovieInputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public final class Movie extends Video {
    private final int duration;

    private HashMap<String, Double> allRatings = new HashMap<>();
    private Double rating;

    public Movie(final MovieInputData movie, final ArrayList<User> users) {
        super();
        setTitle(movie.getTitle());
        setYear(movie.getYear());
        setVideoType(Constants.MOVIES);
        setGenres(movie.getGenres());
        setCast(movie.getCast());
        duration = movie.getDuration();
        allRatings = setupRating(users);
    }

    public int getDuration() {
        return duration;
    }

    public Double getRating() {
        return rating;
    }

    public HashMap<String, Double> getAllRatings() {
        return allRatings;
    }

    private HashMap<String, Double> setupRating(final ArrayList<User> users) {
        for (User user : users) {
            allRatings.put(user.getUsername(), 0.0);
        }
        return allRatings;
    }

    public void changeRating() {
        rating = 0.0;
        for (Map.Entry<String, Double> entry : getAllRatings().entrySet()) {
            if (entry.getValue() != 0) {
                rating = rating + entry.getValue();
            }
        }
        rating = rating / getAllRatings().size();
    }
}
