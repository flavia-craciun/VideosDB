package actions.commands;

import entities.User;
import fileio.ActionInputData;

import java.util.Map;

public final class ViewVideo extends Commands {

    @Override
    public String action(final User user, final ActionInputData action) {
        for (Map.Entry<String, Integer> entry : user.getHistory().entrySet()) {
            if (entry.getKey().equals(action.getTitle())) {
                user.getHistory().replace(entry.getKey(), entry.getValue() + 1);
                getMessage().append("success -> ");
                getMessage().append(entry.getKey());
                getMessage().append(" was viewed with total views of ");
                getMessage().append(entry.getValue());
                return getMessage().toString();
            }
        }
        user.getHistory().put(action.getTitle(), 1);
        getMessage().append("success -> ");
        getMessage().append(action.getTitle());
        getMessage().append(" was viewed with total views of ");
        getMessage().append(user.getHistory().get(action.getTitle()));
        return getMessage().toString();
    }

}
