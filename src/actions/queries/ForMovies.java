package actions.queries;

import common.Constants;
import entities.Entities;
import entities.Movie;
import entities.User;
import fileio.ActionInputData;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.IntStream;

public final class ForMovies extends Query {
    private HashMap<Movie, Double> moviesByCriteria = new HashMap<>();

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

        List<Map.Entry<Movie, Double>> list = sortByValue(moviesByCriteria);

        getMessage().append("Query result: [");
        if (!action.getSortType().equals(Constants.ASCENDENT)) {
            Collections.reverse(list);
        }

        filterResults(action.getFilters(), list);

        if (list.isEmpty()) {
            getMessage().append("]");
            return getMessage().toString();
        }

        List<String> nameList = new ArrayList<>();
        nameList.add(list.get(0).getKey().getTitle());
        Double aux = list.get(0).getValue();
        list.remove(list.get((0)));
        int numberOfResults = 0;

        for (Map.Entry<Movie, Double> entry : list) {
            if (Double.compare(aux, entry.getValue()) != 0) {
                if (nameList.size() > 1) {
                    if (action.getSortType().equals(Constants.ASCENDENT)) {
                        Collections.sort(nameList);
                    } else {
                        Collections.sort(nameList, Collections.reverseOrder());
                    }
                    int size = 0;
                    if (nameList.size() < action.getNumber() - numberOfResults) {
                        size = nameList.size();
                    } else {
                        size = action.getNumber() - numberOfResults;
                    }
                    IntStream.range(0, size).forEach(i -> {
                        getMessage().append(nameList.get(i));
                        getMessage().append(", ");
                    });
                    numberOfResults += size;
                    nameList.clear();
                } else {
                    numberOfResults++;
                    getMessage().append(nameList.get(0));
                    getMessage().append(", ");
                    nameList.remove(nameList.get(0));
                }
                aux = entry.getValue();
                if (numberOfResults >= action.getNumber()) {
                    break;
                }
            }
            nameList.add(entry.getKey().getTitle());
        }

        if (numberOfResults < action.getNumber() && !nameList.isEmpty()) {
            int size = 0;
            if (nameList.size() < action.getNumber() - numberOfResults) {
                size = nameList.size();
            } else {
                size = action.getNumber() - numberOfResults;
            }
            if (action.getSortType().equals(Constants.ASCENDENT)) {
                Collections.sort(nameList);
            } else {
                Collections.sort(nameList, Collections.reverseOrder());
            }
            IntStream.range(0, size).forEach(i -> {
                getMessage().append(nameList.get(i));
                getMessage().append(", ");
            });
        }

        getMessage().replace(getMessage().length() - 2, getMessage().length() - 1, "]");
        getMessage().deleteCharAt(getMessage().length() - 1);

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

    private List<Map.Entry<Movie, Double>> sortByValue(final HashMap<Movie,
            Double> usersRatings) {
        List<Map.Entry<Movie, Double>> list = new LinkedList<>(usersRatings.entrySet());

        Collections.sort(list, Map.Entry.comparingByValue());

        for (Map.Entry<Movie, Double> entry : moviesByCriteria.entrySet()) {
            if (entry.getValue() == 0) {
                list.remove(entry);
            }
        }
        return list;
    }

    private void filterResults(final List<List<String>> filters,
                               final List<Map.Entry<Movie, Double>> list) {
        for (Iterator<Map.Entry<Movie, Double>> iterator = list.iterator(); iterator.hasNext();) {
            Map.Entry<Movie, Double>  entry = iterator.next();
            if (filters.get(1).get(0) != null
                    && !entry.getKey().getGenres().contains(filters.get(1).get(0))) {
                iterator.remove();
            } else {
                if (filters.get(0).get(0) != null
                        && !filters.get(0).contains(String.valueOf(entry.getKey().getYear()))) {
                    iterator.remove();
                }
            }
        }
    }

}
