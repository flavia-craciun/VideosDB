package actions.queries;

import entities.Entities;
import fileio.ActionInputData;

public class Query {
    private Entities entities;
    private StringBuilder message = new StringBuilder();

    public Query(final Entities entities) {
        this.entities = entities;
    }

    public final Entities getEntities() {
        return entities;
    }

    public final void setEntities(final Entities entities) {
        this.entities = entities;
    }

    public final StringBuilder getMessage() {
        return message;
    }

    public String doQuery(final ActionInputData action) {
        return message.toString();
    }

}
