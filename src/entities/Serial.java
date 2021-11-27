package entities;

import common.Constants;
import fileio.SerialInputData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public final class Serial extends Video {

    private final int numberOfSeasons;
    private final ArrayList<Season> seasons = new ArrayList<>();

    public ArrayList<Season> getSeasons() {
        return seasons;
    }

    public Serial(final SerialInputData serial, final ArrayList<User> users) {
        super();
        setTitle(serial.getTitle());
        setYear(serial.getYear());
        setVideoType(Constants.SHOWS);
        setGenres(serial.getGenres());
        setCast(serial.getCast());
        numberOfSeasons = serial.getNumberSeason();
        for (entertainment.Season season: serial.getSeasons()) {
            Season s = new Season(season, users);
            seasons.add(s);
        }
    }

    public final class Season {
        private final int duration;
        private HashMap<String, Double> allRatings = new HashMap<>();
        private static Double averageRating;

        public int getDuration() {
            return duration;
        }

        public HashMap<String, Double> getAllRatings() {
            return allRatings;
        }

        public Double getAverageRating() {
            return averageRating;
        }

        public Season(final entertainment.Season season, final ArrayList<User> users) {
            duration = season.getDuration();
            allRatings = setupRating(users);
        }

        private HashMap<String, Double> setupRating(final ArrayList<User> users) {
            for (User user : users) {
                allRatings.put(user.getUsername(), 0.0);
            }
            return allRatings;
        }

        public void changeRating() {
            averageRating = 0.0;
            for (Map.Entry<String, Double> entry : getAllRatings().entrySet()) {
                if (entry.getValue() != 0) {
                    averageRating = averageRating + entry.getValue();
                }
            }
            averageRating = averageRating / getAllRatings().size();
        }
    }
}
