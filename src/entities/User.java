package entities;

import fileio.UserInputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class User {
    private final String username;
    private final ArrayList<String> favoriteMovies = new ArrayList<>();
    private final String subscriptionType;
    private final HashMap<String, Integer> history = new HashMap<>();

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
        for (String movie : user.getFavoriteMovies()) {
            this.favoriteMovies.add(movie);
        }
        for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
            history.put(entry.getKey(), entry.getValue());
        }
    }

}
