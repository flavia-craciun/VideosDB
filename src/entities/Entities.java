package entities;

import fileio.Input;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;
import fileio.ActorInputData;

import java.util.ArrayList;

public final class Entities {
     private final ArrayList<User> users = new ArrayList<>();
     private final ArrayList<Movie> movies = new ArrayList<>();
     private final ArrayList<Serial> series = new ArrayList<>();
    private final ArrayList<Actor> actors = new ArrayList<>();

    public Entities(final Input input) {
         for (UserInputData user : input.getUsers()) {
             User u = new User(user);
             users.add(u);
         }
         for (MovieInputData movie: input.getMovies()) {
             Movie mov = new Movie(movie, users);
             movies.add(mov);
         }
         for (SerialInputData serial: input.getSerials()) {
             Serial ser = new Serial(serial, users);
             series.add(ser);
         }

        for (ActorInputData actor: input.getActors()) {
            Actor a = new Actor(actor);
            actors.add(a);
        }
     }

    public ArrayList<User> getUsers() {
        return users;
    }

    public ArrayList<Movie> getMovies() {
        return movies;
    }

    public ArrayList<Serial> getSeries() {
        return series;
    }

    public ArrayList<Actor> getActors() {
        return actors;
    }

}
