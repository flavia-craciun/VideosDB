package actions.queries;

import common.Constants;
import entities.Entities;

import entities.Serial;
import entities.User;
import entities.Video;
import fileio.ActionInputData;

import java.util.HashMap;

public final class ForShows extends Query {
    private HashMap<Video, Double> showsByCriteria = new HashMap<>();

    public ForShows(final Entities entities) {
        super(entities);
    }

    @Override
    public String doQuery(final ActionInputData action) {
        switch (action.getCriteria()) {
            case Constants.FAVORITE -> getFavoriteShows();
            case Constants.LONGEST -> getShowsByDuration();
            case Constants.VIEWS -> getShowsByViews();
            default -> getShowsByRating();
        }

        super.composeMessage(showsByCriteria, action);

        return getMessage().toString();
    }

    private void getShowsByRating() {
        for (Serial show : getEntities().getSeries()) {
            if (Double.compare(show.getAverageRating(), 0.0) != 0) {
                showsByCriteria.put(show, show.getAverageRating());
            }
        }
    }

    private void getFavoriteShows() {
        for (Serial show : getEntities().getSeries()) {
            Double numberOfApparitions = 0.0;
            for (User user : getEntities().getUsers()) {
                if (user.getFavoriteMovies().contains(show.getTitle())) {
                    numberOfApparitions++;
                }
            }
            showsByCriteria.put(show, numberOfApparitions);
        }
    }

    private void getShowsByDuration() {
        for (Serial show : getEntities().getSeries()) {
            Double showDuration = 0.0;
            for (Serial.Season season : show.getSeasons()) {
                showDuration = showDuration + season.getDuration();
            }
            showsByCriteria.put(show, showDuration);
        }
    }

    private void getShowsByViews() {
        for (Serial show : getEntities().getSeries()) {
            Double numberOfViews = 0.0;
            for (User user : getEntities().getUsers()) {
                if (user.getHistory().containsKey(show.getTitle())) {
                    numberOfViews = numberOfViews + user.getHistory().get(show.getTitle());
                }
            }
            showsByCriteria.put(show, numberOfViews);
        }
    }

}
