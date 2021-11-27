package entities;

import fileio.UserInputData;

import java.util.ArrayList;
import java.util.Map;

public final class User {
    private final String username;
    private final ArrayList<String> favoriteMovies;
    private final String subscriptionType;
    private final Map<String, Integer> history;

    public String getUsername() {
        return username;
    }

    public ArrayList<String> getFavoriteMovies() {
        return favoriteMovies;
    }

    public String getSubscriptionType() {
        return subscriptionType;
    }

    public Map<String, Integer> getHistory() {
        return history;
    }

    public User(final UserInputData user) {
        this.username = user.getUsername();
        this.subscriptionType = user.getSubscriptionType();
        this.favoriteMovies = user.getFavoriteMovies();
        this.history = user.getHistory();
        for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
            getHistory().put(entry.getKey(), entry.getValue());
        }
    }

}
