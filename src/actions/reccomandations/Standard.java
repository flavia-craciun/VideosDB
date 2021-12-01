package actions.reccomandations;

import entities.Entities;
import entities.Movie;
import entities.User;
import fileio.ActionInputData;

public class Standard extends Reccommendation {

    public Standard(final Entities allEntities) {
        super(allEntities);
    }

    @Override
    public final String getReccomendation(final ActionInputData action) {
        for (User user : getDatabase().getUsers()) {
            if (user.getUsername().compareTo(action.getUsername()) == 0) {
                for (Movie movie : getDatabase().getMovies()) {
                    if (!user.getHistory().containsKey(movie.getTitle())) {
                        getMessage().append("StandardRecommendation result: ");
                        getMessage().append(movie.getTitle());
                        return getMessage().toString();
                    }
                }
            }
        }
        getMessage().append("StandardRecommendation cannot be applied!");
        return getMessage().toString();
    }
}
