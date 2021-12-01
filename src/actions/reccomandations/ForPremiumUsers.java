package actions.reccomandations;

import common.Constants;
import entertainment.Genre;

import entities.Entities;
import entities.Movie;
import entities.User;
import entities.Video;
import entities.Serial;
import fileio.ActionInputData;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.LinkedHashMap;
import java.util.stream.IntStream;

public final class ForPremiumUsers extends Reccommendation {

    public ForPremiumUsers(final Entities allEntities) {
        super(allEntities);
    }

    @Override
    public String getReccomendation(final ActionInputData action) {
        for (User user : getDatabase().getUsers()) {
             if (user.getUsername().compareTo(action.getUsername()) == 0) {
                 switch (action.getType()) {
                     case Constants.POPULAR -> getPopularVideo(user);
                     case Constants.FAVORITE -> getFavoriteVideo(user);
                     case Constants.SEARCH -> getVideoBySearch(user, action);
                     default -> super.getReccomendation(action);
                    }
                }
            }
        return getMessage().toString();
    }

    private void getPopularVideo(final User user) {
        if (user.getSubscriptionType().compareTo(Constants.PREMIUM) != 0) {
            getMessage().append("PopularRecommendation cannot be applied!");
            return;
        }
        HashMap<Genre, Integer> popularGenre = new HashMap<>();
        LinkedHashMap<Genre, ArrayList<Video>> videosByGenre = new LinkedHashMap<>();
        getMessage().append("PopularRecommendation result: ");
        for (Genre genre : Genre.values()) {
            ArrayList<Video> list = new ArrayList<>();
            int numberOfVideos = 0;
            String word = genre.toString();
            String newWord = genre.toString().substring(1);
            word = word.replace(word.substring(1), newWord.toLowerCase());
            for (Movie movie : getDatabase().getMovies()) {
                if (!user.getHistory().containsKey(movie.getTitle())) {
                    if (movie.getGenres().contains(word)) {
                        list.add(movie);
                        numberOfVideos++;
                    }
                }
            }
            for (Serial show : getDatabase().getSeries()) {
                if (!user.getHistory().containsKey(show.getTitle())) {
                    if (show.getGenres().contains(word)) {
                        list.add(show);
                        numberOfVideos++;
                    }
                }
            }
            popularGenre.put(genre, numberOfVideos);
            videosByGenre.put(genre, list);
        }

        List<Map.Entry<Genre, Integer>> list = new LinkedList<>(popularGenre.entrySet());
        Collections.sort(list, Map.Entry.comparingByValue());
        Collections.reverse(list);
        for (Map.Entry<Genre, Integer> entry : popularGenre.entrySet()) {
            if (entry.getValue() == 0) {
                list.remove(entry);
            }
        }

        if (list.isEmpty()) {
            getMessage().replace(0, getMessage().length(),
                    "PopularRecommendation cannot be applied!");
            return;
        }

        int i = 0;
        while (i < list.size()) {
            for (Video video: videosByGenre.get(list.get(i).getKey())) {
                if (!user.getHistory().containsKey(video.getTitle())) {
                    getMessage().append(video.getTitle());
                    return;
                }
            }
            i++;
        }
    }

    private void getFavoriteVideo(final User user) {
        if (user.getSubscriptionType().compareTo(Constants.PREMIUM) != 0) {
            getMessage().append("FavoriteRecommendation cannot be applied!");
            return;
        }
        LinkedHashMap<Video, Double> videosByFavorites = new LinkedHashMap<>();
        getMessage().append("FavoriteRecommendation result: ");
        for (Movie movie : getDatabase().getMovies()) {
            Double numberOfApparitions = 0.0;
            if (!user.getHistory().containsKey(movie.getTitle())) {
                for (User u : getDatabase().getUsers()) {
                    if (u.getFavoriteMovies().contains(movie.getTitle())) {
                        numberOfApparitions++;
                    }
                }
            videosByFavorites.put(movie, numberOfApparitions);
            }
        }
        for (Serial show : getDatabase().getSeries()) {
            Double numberOfApparitions = 0.0;
            if (!user.getHistory().containsKey(show.getTitle())) {
                for (User u : getDatabase().getUsers()) {
                    if (u.getFavoriteMovies().contains(show.getTitle())) {
                        numberOfApparitions++;
                    }
                }
            videosByFavorites.put(show, numberOfApparitions);
            }
        }

        List<Map.Entry<Video, Double>> list = sortByValue(videosByFavorites);
        Collections.reverse(list);
        if (list.isEmpty()) {
            getMessage().replace(0, getMessage().length(),
                    "FavoriteRecommendation cannot be applied!");
        } else {
            List<String> nameList = new ArrayList<>();
            nameList.add(list.get(0).getKey().getTitle());
            Double aux = list.get(0).getValue();
            for (Map.Entry<Video, Double> entry : list) {
                if (Double.compare(aux, entry.getValue()) != 0) {
                    if (nameList.size() > 1) {
                        getMessage().append(nameList.get(nameList.size() - 1));
                    } else {
                        getMessage().append(nameList.get(0));
                    }
                    return;
                }
                nameList.add(entry.getKey().getTitle());
            }

            if (!nameList.isEmpty()) {
                getMessage().append(nameList.get(nameList.size() - 1));
            }
        }
    }

    private void getVideoBySearch(final User user, final ActionInputData action) {
        if (user.getSubscriptionType().compareTo(Constants.PREMIUM) != 0) {
            getMessage().append("SearchRecommendation cannot be applied!");
            return;
        }
        LinkedHashMap<Video, Double> videosBySearch = new LinkedHashMap<>();
        getMessage().append("SearchRecommendation result: [");
        for (Movie movie : getDatabase().getMovies()) {
            if (movie.getGenres().contains(action.getGenre())
                && !user.getHistory().containsKey(movie.getTitle())) {
                videosBySearch.put(movie, movie.getAverageRating());
            }
        }
        for (Serial show : getDatabase().getSeries()) {
            if (show.getGenres().contains(action.getGenre())
                    && !user.getHistory().containsKey(show.getTitle())) {
                videosBySearch.put(show, show.getAverageRating());
            }
        }
        List<Map.Entry<Video, Double>> list = sortByValue(videosBySearch);

        if (list.isEmpty()) {
            getMessage().replace(0, getMessage().length(),
                    "SearchRecommendation cannot be applied!");
            return;
        }

        List<String> nameList = new ArrayList<>();
        nameList.add(list.get(0).getKey().getTitle());
        Double aux = list.get(0).getValue();
        list.remove(list.get((0)));

        for (Map.Entry<Video, Double> entry : list) {
            if (Double.compare(aux, entry.getValue()) != 0) {
                if (nameList.size() > 1) {
                    Collections.sort(nameList);
                    IntStream.range(0, nameList.size()).forEach(i -> {
                        getMessage().append(nameList.get(i));
                        getMessage().append(", ");
                    });
                    nameList.clear();
                } else {
                    getMessage().append(nameList.get(0));
                    getMessage().append(", ");
                    nameList.remove(nameList.get(0));
                }
                aux = entry.getValue();
            }
            nameList.add(entry.getKey().getTitle());
        }

        if (!nameList.isEmpty()) {
            Collections.sort(nameList);
            IntStream.range(0, nameList.size()).forEach(i -> {
                getMessage().append(nameList.get(i));
                getMessage().append(", ");
            });
        }

        getMessage().replace(getMessage().length() - 2, getMessage().length() - 1, "]");
        getMessage().deleteCharAt(getMessage().length() - 1);
    }

    private List<Map.Entry<Video, Double>> sortByValue(final LinkedHashMap<Video, Double> videos) {
        List<Map.Entry<Video, Double>> list = new LinkedList<>(videos.entrySet());

        Collections.sort(list, Map.Entry.comparingByValue());

        return list;
    }
}
