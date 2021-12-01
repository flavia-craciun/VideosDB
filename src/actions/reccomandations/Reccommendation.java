package actions.reccomandations;

import entities.Entities;
import fileio.ActionInputData;

public class Reccommendation {
    private Entities database;
    private StringBuilder message = new StringBuilder();

    public Reccommendation(final Entities allEntities) {
        database = allEntities;
    }

    public StringBuilder getMessage() {
        return message;
    }

    public Entities getDatabase() {
        return database;
    }

    public String getReccomendation(final ActionInputData action) {
        return message.toString();
    }
}
