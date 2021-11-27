package entities;

import fileio.Input;
import fileio.MovieInputData;
import fileio.SerialInputData;
import fileio.UserInputData;

import java.util.ArrayList;

public final class Entities {
     private final ArrayList<User> users = new ArrayList<>();
     private final ArrayList<Movie> movies = new ArrayList<>();
     private final ArrayList<Serial> series = new ArrayList<>();

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
}
