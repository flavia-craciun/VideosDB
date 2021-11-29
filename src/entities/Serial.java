package entities;

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
        setGenres(serial.getGenres());
        setCast(serial.getCast());
        numberOfSeasons = serial.getNumberSeason();
        for (entertainment.Season season: serial.getSeasons()) {
            Season s = new Season(season, users);
            seasons.add(s);
        }
    }

    @Override
    public Double getAverageRating() {
        Double showAverage = 0.0;
        for (Season season: getSeasons()) {
                showAverage = showAverage + season.getAverageRating();
            }
        showAverage = showAverage / numberOfSeasons;
        return showAverage;
    }

    public final class Season {
        private final int duration;
        private HashMap<String, Double> allRatings = new HashMap<>();

        public int getDuration() {
            return duration;
        }

        public HashMap<String, Double> getAllRatings() {
            return allRatings;
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

        public Double getAverageRating() {
            Double averageRating = 0.0;
            int numberOfUsers = 0;
            for (Map.Entry<String, Double> entry : getAllRatings().entrySet()) {
                if (entry.getValue() != 0) {
                    numberOfUsers++;
                    averageRating = averageRating + entry.getValue();
                }
            }
            if (numberOfUsers != 0) {
                averageRating = averageRating / numberOfUsers;
            }
            return averageRating;
        }
    }
}
