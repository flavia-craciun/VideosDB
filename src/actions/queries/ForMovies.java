package actions.queries;

import common.Constants;
import entities.Entities;
import entities.Movie;
import entities.User;
import entities.Video;
import fileio.ActionInputData;

import java.util.HashMap;

public final class ForMovies extends Query {
    private HashMap<Video, Double> moviesByCriteria = new HashMap<>();

    public ForMovies(final Entities entities) {
        super(entities);
    }

    @Override
    public String doQuery(final ActionInputData action) {
        switch (action.getCriteria()) {
            case Constants.FAVORITE -> getFavoriteMovies();
            case Constants.LONGEST -> getMoviesByDuration();
            case Constants.VIEWS -> getMoviesByViews();
            default -> getMovieByRating();
        }

        super.composeMessage(moviesByCriteria, action);

        return getMessage().toString();
    }

    private void getMovieByRating() {
        for (Movie movie : getEntities().getMovies()) {
            if (Double.compare(movie.getAverageRating(), 0.0) != 0) {
                moviesByCriteria.put(movie, movie.getAverageRating());
            }
        }
    }

    private void getFavoriteMovies() {
        for (Movie movie : getEntities().getMovies()) {
            Double numberOfApparitions = 0.0;
            for (User user : getEntities().getUsers()) {
                if (user.getFavoriteMovies().contains(movie.getTitle())) {
                    numberOfApparitions++;
                }
            }
            moviesByCriteria.put(movie, numberOfApparitions);
        }
    }

    private void getMoviesByDuration() {
        for (Movie movie : getEntities().getMovies()) {
            moviesByCriteria.put(movie, (double) movie.getDuration());
        }
    }

    private void getMoviesByViews() {
        for (Movie movie : getEntities().getMovies()) {
            Double numberOfViews = 0.0;
            for (User user : getEntities().getUsers()) {
                if (user.getHistory().containsKey(movie.getTitle())) {
                    numberOfViews = numberOfViews + user.getHistory().get(movie.getTitle());
                }
            }
            moviesByCriteria.put(movie, numberOfViews);
        }
    }
}
