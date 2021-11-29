package actions.queries;

import common.Constants;
import entities.Entities;
import entities.Movie;
import entities.Serial;
import entities.User;
import fileio.ActionInputData;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.IntStream;


public final class ForUsers extends Query {
    private HashMap<String, Integer> numberOfRatings = new HashMap<>();

    public ForUsers(final Entities entities) {
        super(entities);
    }

    @Override
    public String doQuery(final ActionInputData action) {
        for (User user : getEntities().getUsers()) {
            getUserByRating(user);
        }
        List<Map.Entry<String, Integer>> list = sortByValue(numberOfRatings);

        getMessage().append("Query result: [");
        if (!action.getSortType().equals(Constants.ASCENDENT)) {
            Collections.reverse(list);
        }

        if (list.isEmpty()) {
            getMessage().append("]");
            return getMessage().toString();
        }

        List<String> nameList = new ArrayList<>();
        nameList.add(list.get(0).getKey());
        Integer aux = list.get(0).getValue();
        list.remove(list.get((0)));
        int numberOfResults = 0;

        for (Map.Entry<String, Integer> entry : list) {
            if (Double.compare(aux, entry.getValue()) != 0) {
                if (nameList.size() > 1) {
                    if (action.getSortType().equals(Constants.ASCENDENT)) {
                        Collections.sort(nameList);
                    } else {
                        Collections.reverse(nameList);
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
            nameList.add(entry.getKey());
        }

        if (numberOfResults < action.getNumber() && !nameList.isEmpty()) {
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
        }

        getMessage().replace(getMessage().length() - 2, getMessage().length() - 1, "]");
        getMessage().deleteCharAt(getMessage().length() - 1);

        return getMessage().toString();
    }

    private void getUserByRating(final User user) {
        int numberOfRatedVideos = 0;
        for (Movie movie : getEntities().getMovies()) {
            if (movie.getAllRatings().get(user.getUsername()) != 0.0) {
                numberOfRatedVideos = numberOfRatedVideos + 1;
            }
        }
        for (Serial show : getEntities().getSeries()) {
            for (Serial.Season season : show.getSeasons()) {
                if (season.getAllRatings().get(user.getUsername()) != 0.0) {
                    numberOfRatedVideos = numberOfRatedVideos + 1;
                }
            }
        }
        numberOfRatings.put(user.getUsername(), numberOfRatedVideos);
    }

    private List<Map.Entry<String, Integer>> sortByValue(final HashMap<String,
            Integer> usersRatings) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(usersRatings.entrySet());

        Collections.sort(list, Map.Entry.comparingByValue());

        for (Map.Entry<String, Integer> entry : numberOfRatings.entrySet()) {
            if (entry.getValue() == 0) {
                list.remove(entry);
            }
        }
        return list;
    }
}
