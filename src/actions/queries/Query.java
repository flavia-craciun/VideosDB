package actions.queries;

import common.Constants;
import entities.Entities;
import entities.Video;
import fileio.ActionInputData;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.IntStream;

public class Query {
    private Entities entities;
    private StringBuilder message = new StringBuilder();

    public Query(final Entities entities) {
        this.entities = entities;
    }

    public final Entities getEntities() {
        return entities;
    }

    public final void setEntities(final Entities entities) {
        this.entities = entities;
    }

    public final StringBuilder getMessage() {
        return message;
    }

    public String doQuery(final ActionInputData action) {
        return message.toString();
    }

    public void composeMessage(HashMap<Video, Double> videos, ActionInputData action) {
        List<Map.Entry<Video, Double>> list = sortByValue(videos);

        getMessage().append("Query result: [");
        if (!action.getSortType().equals(Constants.ASCENDENT)) {
            Collections.reverse(list);
        }

        filterResults(action.getFilters(), list);

        if (list.isEmpty()) {
            getMessage().append("]");
            return;
        }

        List<String> nameList = new ArrayList<>();
        nameList.add(list.get(0).getKey().getTitle());
        Double aux = list.get(0).getValue();
        list.remove(list.get((0)));
        int numberOfResults = 0;

        for (Map.Entry<Video, Double> entry : list) {
            if (Double.compare(aux, entry.getValue()) != 0) {
                if (nameList.size() > 1) {
                    printTitles(action, nameList, numberOfResults);
                    numberOfResults += calculateSize(numberOfResults, nameList, action);
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
            printTitles(action, nameList, numberOfResults);
        }

        getMessage().replace(getMessage().length() - 2, getMessage().length() - 1, "]");
        getMessage().deleteCharAt(getMessage().length() - 1);
    }

    private List<Map.Entry<Video, Double>> sortByValue(final HashMap<Video,
            Double> usersRatings) {
        List<Map.Entry<Video, Double>> list = new LinkedList<>(usersRatings.entrySet());

        Collections.sort(list, Map.Entry.comparingByValue());

        for (Map.Entry<Video, Double> entry : usersRatings.entrySet()) {
            if (entry.getValue() == 0) {
                list.remove(entry);
            }
        }
        return list;
    }

    private void filterResults(final List<List<String>> filters,
                               final List<Map.Entry<Video, Double>> list) {
        for (Iterator<Map.Entry<Video, Double>> iterator = list.iterator(); iterator.hasNext();) {
            Map.Entry<Video, Double>  entry = iterator.next();
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

    public void printTitles(final ActionInputData action, final List<String> nameList,
                             final int numberOfResults) {
        int size = calculateSize(numberOfResults, nameList, action);
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

    public Integer calculateSize(final int numberOfResults, final List<String> nameList,
                                  final ActionInputData action) {
        return Math.min(nameList.size(), action.getNumber() - numberOfResults);
    }

}
