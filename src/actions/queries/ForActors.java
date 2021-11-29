package actions.queries;

import actor.ActorsAwards;
import common.Constants;
import entities.Actor;
import entities.Entities;
import entities.Movie;
import entities.Serial;
import fileio.ActionInputData;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.stream.IntStream;

public final class ForActors extends Query {
    private HashMap<Actor, Double> actorsByCriteria = new HashMap<>();

    public ForActors(final Entities entities) {
        super(entities);
    }

    @Override
    public String doQuery(final ActionInputData action) {
        List<Map.Entry<Actor, Double>> list;

        switch (action.getCriteria()) {
            case Constants.AWARDS -> getActorsByAwards(action);
            case Constants.FILTER_DESCRIPTIONS -> getActorByDescription(action);
            default -> getActorsByAverageRating();
        }

        list = sortByValue(actorsByCriteria);

        getMessage().append("Query result: [");
        if (!action.getSortType().equals(Constants.ASCENDENT)) {
            Collections.reverse(list);
        }

        if (list.isEmpty()) {
            getMessage().append("]");
            return getMessage().toString();
        }

        List<String> nameList = new ArrayList<>();
        nameList.add(list.get(0).getKey().getName());
        Double aux = list.get(0).getValue();
        list.remove(list.get((0)));
        int numberOfResults = 0;

        for (Map.Entry<Actor, Double> entry : list) {
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
            nameList.add(entry.getKey().getName());
        }

        if (numberOfResults < action.getNumber() && !nameList.isEmpty()) {
            int size = 0;
            if (nameList.size() < action.getNumber() - numberOfResults) {
                size = nameList.size();
            } else {
                size = action.getNumber() - numberOfResults;
                IntStream.range(0, size).forEach(i -> {
                    getMessage().append(nameList.get(i));
                    getMessage().append(", ");
                });
            }
        }

        getMessage().replace(getMessage().length() - 2, getMessage().length() - 1, "]");
        getMessage().deleteCharAt(getMessage().length() - 1);

        return getMessage().toString();
}

    private void getActorsByAverageRating() {
        for (Actor actor : getEntities().getActors()) {
            int numberOfRatedVideos = 0;
            Double averageRating = 0.0;
            for (Movie movie : getEntities().getMovies()) {
                if (movie.getCast().contains(actor.getName())
                        && Double.compare(movie.getAverageRating(), 0.0) != 0) {
                    numberOfRatedVideos++;
                    averageRating = averageRating + movie.getAverageRating();
                }
            }
            for (Serial show : getEntities().getSeries()) {
                if (show.getCast().contains(actor.getName())
                        && Double.compare(show.getAverageRating(), 0.0) != 0) {
                    numberOfRatedVideos++;
                    averageRating = averageRating + show.getAverageRating();
                }
            }
            if (numberOfRatedVideos != 0) {
                averageRating = averageRating / numberOfRatedVideos;
            }
            actorsByCriteria.put(actor, averageRating);
        }
    }

    private void getActorsByAwards(final ActionInputData action) {
        for (Actor actor : getEntities().getActors()) {
            Double numberOfAwards = 0.0;
            for (String nameOfAward : action.getFilters().get(3)) {
                boolean gotAward = false;
                for (ActorsAwards award : actor.getAwards().keySet()) {
                    if (award.name().compareTo(nameOfAward) == 0) {
                        gotAward = true;
                        numberOfAwards = numberOfAwards + actor.getAwards().get(award);
                        break;
                    }
                }
                if (!gotAward) {
                    numberOfAwards = 0.0;
                    break;
                }
            }
            actorsByCriteria.put(actor, numberOfAwards);
        }
    }

    private void getActorByDescription(final ActionInputData action) {
        for (Actor actor: getEntities().getActors()) {
            actorsByCriteria.put(actor, 0.0);
            for (String word : action.getFilters().get(2)) {
                if (!actor.getCareerDescription().contains(word)) {
                    actorsByCriteria.remove(actor);
                    break;
                }
            }
        }
    }

    private List<Map.Entry<Actor, Double>> sortByValue(final HashMap<Actor, Double> actorRatings) {
        List<Map.Entry<Actor, Double>> list = new LinkedList<>(actorRatings.entrySet());

        Collections.sort(list, Map.Entry.comparingByValue());

        for (Map.Entry<Actor, Double> entry : actorRatings.entrySet()) {
            if (entry.getValue() == 0) {
                list.remove(entry);
            }
        }
        return list;
    }
}
