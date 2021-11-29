package actions.commands;

import entities.User;
import fileio.ActionInputData;

public class Commands {
    private final StringBuilder message = new StringBuilder();

    public final StringBuilder getMessage() {
        return message;
    }

    public String action(final User user, final ActionInputData action) {
        return message.toString();
    }

}
