package entities;

import fileio.MovieInputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public final class Movie extends Video {
    private final int duration;
    private HashMap<String, Double> allRatings = new HashMap<>();

    public Movie(final MovieInputData movie, final ArrayList<User> users) {
        super();
        setTitle(movie.getTitle());
        setYear(movie.getYear());
        setGenres(movie.getGenres());
        setCast(movie.getCast());
        duration = movie.getDuration();
        allRatings = setupRating(users);
    }

    public int getDuration() {
        return duration;
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

    @Override
    public Double getAverageRating() {
        Double averageRating = 0.0;
        int numberOfUsers = 0;
        for (Map.Entry<String, Double> entry : getAllRatings().entrySet()) {
            if (entry.getValue() != 0) {
                numberOfUsers++;
                averageRating = averageRating + entry.getValue();
            }
        }
        if (numberOfUsers != 0) {
            averageRating = averageRating / numberOfUsers;
        }
        return averageRating;
    }
}
