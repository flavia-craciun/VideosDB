package actions.reccomandations;

import entities.Entities;
import entities.Movie;
import entities.User;
import entities.Serial;
import entities.Video;
import fileio.ActionInputData;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.LinkedList;

public final class BestUnseen extends Reccommendation {
    private LinkedHashMap<Video, Double> videosByRating = new LinkedHashMap<>();

    public BestUnseen(final Entities allEntities) {
        super(allEntities);
    }

    @Override
    public String getReccomendation(final ActionInputData action) {
        getVideosByRating();
        List<Map.Entry<Video, Double>> list = new LinkedList<>(videosByRating.entrySet());
        Collections.sort(list, Map.Entry.comparingByValue());
        Collections.reverse(list);

        for (User user: getDatabase().getUsers()) {
            if (user.getUsername().compareTo(action.getUsername()) == 0) {
                for (Map.Entry<Video, Double> entry : videosByRating.entrySet()) {
                    if (user.getHistory().containsKey(entry.getKey().getTitle())) {
                        list.remove(entry);
                    }
                }
                break;
            }
        }

        if (list.isEmpty()) {
            getMessage().append("BestRatedUnseenRecommendation cannot be applied!");
            return getMessage().toString();
        }

        getMessage().append("BestRatedUnseenRecommendation result: ");

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
                return getMessage().toString();
            }
            nameList.add(entry.getKey().getTitle());
        }

        if (!nameList.isEmpty()) {
            getMessage().append(nameList.get(nameList.size() - 1));
        }

        return getMessage().toString();
    }

    private void getVideosByRating() {
        for (Movie movie : getDatabase().getMovies()) {
            videosByRating.put(movie, movie.getAverageRating());
        }
        for (Serial show : getDatabase().getSeries()) {
            videosByRating.put(show, show.getAverageRating());
        }
    }

}
