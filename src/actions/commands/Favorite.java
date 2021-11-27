package actions.commands;

import entities.User;
import fileio.ActionInputData;

import java.util.Map;

public final class Favorite extends Commands {

    @Override
    public  String action(final User user, final ActionInputData action) {
        for (String movie : user.getFavoriteMovies()) {
            if (movie.equals(action.getTitle())) {
                getMessage().append("error -> ");
                getMessage().append(action.getTitle());
                getMessage().append(" is already in favourite list");
                return getMessage().toString();
            }
        }
        for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
            if (entry.getKey().equals(action.getTitle())) {
                user.getFavoriteMovies().add(action.getTitle());
                getMessage().append("success -> ");
                getMessage().append(action.getTitle());
                getMessage().append(" was added as favourite");
                return getMessage().toString();
            }
        }
        getMessage().append("error -> ");
        getMessage().append(action.getTitle());
        getMessage().append(" is not seen");
        return getMessage().toString();
    }
}
