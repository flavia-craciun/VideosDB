package actions.queries;

import common.Constants;
import entities.Entities;

import entities.Serial;
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

public final class ForShows extends Query {
    private HashMap<Serial, Double> showsByCriteria = new HashMap<>();

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

            List<Map.Entry<Serial, Double>> list = sortByValue(showsByCriteria);

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

            for (Map.Entry<Serial, Double> entry : list) {
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
                nameList.add(entry.getKey().getTitle());
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

    private List<Map.Entry<Serial, Double>> sortByValue(final HashMap<Serial,
            Double> usersRatings) {
        List<Map.Entry<Serial, Double>> list = new LinkedList<>(usersRatings.entrySet());

        Collections.sort(list, Map.Entry.comparingByValue());

        for (Map.Entry<Serial, Double> entry : showsByCriteria.entrySet()) {
            if (entry.getValue() == 0) {
                list.remove(entry);
            }
        }
        return list;
    }

    private void filterResults(final List<List<String>> filters,
                               final List<Map.Entry<Serial, Double>> list) {
        for (Iterator<Map.Entry<Serial, Double>> iterator = list.iterator(); iterator.hasNext();) {
            Map.Entry<Serial, Double>  entry = iterator.next();
            if (!filters.get(1).equals(null)
                    && !entry.getKey().getGenres().contains(filters.get(1).get(0))) {
                iterator.remove();
            } else {
                if (!filters.get(0).isEmpty()
                        && !filters.get(0).contains(String.valueOf(entry.getKey().getYear()))) {
                    iterator.remove();
                }
            }
        }
    }

}
