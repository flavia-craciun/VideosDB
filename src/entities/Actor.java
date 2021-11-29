package entities;

import actor.ActorsAwards;
import fileio.ActorInputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class Actor {
    private String name;
    private String careerDescription;
    private ArrayList<String> filmography = new ArrayList<>();
    private HashMap<ActorsAwards, Integer> awards = new HashMap<>();

    public Actor(final ActorInputData actor) {
        this.name = actor.getName();
        this.careerDescription = actor.getCareerDescription();
        for (String movie : actor.getFilmography()) {
            filmography.add(movie);
        }
        for (Map.Entry<ActorsAwards, Integer> entry : actor.getAwards().entrySet()) {
            awards.put(entry.getKey(), entry.getValue());
        }
    }

    public String getName() {
        return name;
    }

    public String getCareerDescription() {
        return careerDescription;
    }

    public ArrayList<String> getFilmography() {
        return filmography;
    }

    public HashMap<ActorsAwards, Integer> getAwards() {
        return awards;
    }
}
