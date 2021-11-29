package actions.commands;

import entities.Entities;
import entities.Movie;
import entities.Serial;
import entities.User;
import fileio.ActionInputData;

public final class Rate extends Commands {

    private final Entities entities;

    public Rate(final Entities entity) {
        this.entities = entity;
    }

    @Override
    public String action(final User user, final ActionInputData action) {
        entities.getMovies().stream()
                .filter(movie -> movie.getTitle().equals(action.getTitle()))
                .findAny().ifPresent(rateMovie -> rateMovie(user, action));
        entities.getSeries().stream()
                .filter(show -> show.getTitle().equals(action.getTitle()))
                .findAny().ifPresent(rateShow -> rateShow(user, action));
        return getMessage().toString();
    }

    private void rateMovie(final User user, final ActionInputData action) {
        if (!user.getHistory().containsKey(action.getTitle())) {
            getMessage().append("error -> ");
            getMessage().append(action.getTitle());
            getMessage().append(" is not seen");
        } else {
            for (Movie movie : entities.getMovies()) {
                if (movie.getTitle().equals(action.getTitle())) {
                    if (Double.compare(movie.getAllRatings().get(action.getUsername()), 0.0) == 0) {
                        movie.getAllRatings().replace(action.getUsername(), action.getGrade());
                        getMessage().append("success -> ");
                        getMessage().append(movie.getTitle());
                        getMessage().append(" was rated with ");
                        getMessage().append(movie.getAllRatings().get(action.getUsername()));
                        getMessage().append(" by ");
                        getMessage().append(action.getUsername());
                    } else {
                        getMessage().append("error -> ");
                        getMessage().append(movie.getTitle());
                        getMessage().append(" has been already rated");
                    }
                }
            }
        }
    }

    private void rateShow(final User user, final ActionInputData action) {
        if (!user.getHistory().containsKey(action.getTitle())) {
            getMessage().append("error -> ");
            getMessage().append(action.getTitle());
            getMessage().append(" is not seen");
        } else {
            for (Serial show : entities.getSeries()) {
                if (show.getTitle().equals(action.getTitle())) {
                    if (Double.compare(show.getSeasons().get(action.getSeasonNumber() - 1)
                            .getAllRatings().get(action.getUsername()), 0.0) == 0) {
                        show.getSeasons().get(action.getSeasonNumber() - 1).getAllRatings()
                                .replace(action.getUsername(), action.getGrade());
                        getMessage().append("success -> ");
                        getMessage().append(show.getTitle());
                        getMessage().append(" was rated with ");
                        getMessage().append(show.getSeasons().get(action.getSeasonNumber() - 1)
                                .getAllRatings().get(action.getUsername()));
                        getMessage().append(" by ");
                        getMessage().append(action.getUsername());
                    } else {
                        getMessage().append("error -> ");
                        getMessage().append(show.getTitle());
                        getMessage().append(" has been already rated");
                    }
                }
            }
        }
    }

}
