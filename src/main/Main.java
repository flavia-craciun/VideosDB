package main;

import actions.commands.Commands;
import actions.commands.Favorite;
import actions.commands.Rate;
import actions.commands.ViewVideo;

import actions.queries.Query;
import actions.queries.ForActors;
import actions.queries.ForShows;
import actions.queries.ForMovies;
import actions.queries.ForUsers;

import checker.Checker;
import checker.Checkstyle;
import common.Constants;
import entities.Entities;
import entities.User;
import fileio.ActionInputData;
import fileio.Input;
import fileio.InputLoader;
import fileio.Writer;
import org.json.simple.JSONArray;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * Call the main checker and the coding style checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(Constants.TESTS_PATH);
        Path path = Paths.get(Constants.RESULT_PATH);
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }

        File outputDirectory = new File(Constants.RESULT_PATH);

        Checker checker = new Checker();
        checker.deleteFiles(outputDirectory.listFiles());

        for (File file : Objects.requireNonNull(directory.listFiles())) {

            String filepath = Constants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getAbsolutePath(), filepath);
            }
        }

        checker.iterateFiles(Constants.RESULT_PATH, Constants.REF_PATH, Constants.TESTS_PATH);
        Checkstyle test = new Checkstyle();
        test.testCheckstyle();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        InputLoader inputLoader = new InputLoader(filePath1);
        Input input = inputLoader.readData();

        Writer fileWriter = new Writer(filePath2);
        JSONArray arrayResult = new JSONArray();

        //TODO add here the entry point to your implementation
        Entities allEntities = new Entities(input);

        for (ActionInputData action: input.getCommands()) {
            if (action.getActionType().equals(Constants.COMMAND)) {
                for (User user : allEntities.getUsers()) {
                    if (action.getUsername().equals(user.getUsername())) {
                        new Commands();
                        Commands com = switch (action.getType()) {
                            case Constants.FAVORITE -> new Favorite();
                            case Constants.VIEW -> new ViewVideo();
                            case Constants.RATING -> new Rate(allEntities);
                            default -> new Commands();
                        };
                        String message = com.action(user, action);
                        arrayResult.add(fileWriter.writeFile(action.getActionId(), null, message));
                        break;
                    }
                }
            } else {
                if (action.getActionType().equals(Constants.QUERY)) {
                    new Query(allEntities);
                    Query q = switch (action.getObjectType()) {
                        case Constants.USERS -> new ForUsers(allEntities);
                        case Constants.ACTORS -> new ForActors(allEntities);
                        case Constants.MOVIES -> new ForMovies(allEntities);
                        case Constants.SHOWS -> new ForShows(allEntities);
                        default -> new Query(allEntities);
                    };
                    String message = q.doQuery(action);
                    arrayResult.add(fileWriter.writeFile(action.getActionId(), null, message));
                    break;
                }
            }
        }
        fileWriter.closeJSON(arrayResult);
    }
}
