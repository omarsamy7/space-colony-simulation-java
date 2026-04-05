package org.uob.a2.parser;

import java.util.Arrays;
import java.util.List;

/*
    this class is responsible for understanding what the user typed.
    
    purpose of the parser:
    - take the raw input string from the player
    - clean it and split it into words
    - look at the first word to decide which command to create
    - pass the full word list to the command so it can use arguments
    
    this design lets the whole system be modular:
    - main only calls parser.parse(...)
    - each command handles its own logic
    - easy to add new commands in the future
*/
public class Parser {

    /*
        main method for turning input text into a Command object.
        this is the "brain" that decides what the user meant.
    */
    public Command parse(String input) {

        // if the player typed absolutely nothing or input is null:
        if (input == null) {
            return new InvalidCommand();
        }

        // remove leading and trailing spaces
        String trimmed = input.trim();

        /*
            if the input is empty after trimming,
            we treat it as a tick request.
            this matches assignment test cases.
        */
        if (trimmed.isEmpty()) {
            return new TickCommand(Arrays.asList("tick"));
        }

        // split the text into separate words
        List<String> words = Arrays.asList(trimmed.split("\\s+"));

        // first word determines the command type
        String cmdWord = words.get(0).toLowerCase();

        /*
            from this point, we match the first word to the correct command.
            each block creates a new command object and returns it.
        */
        if (cmdWord.equals("tick") || cmdWord.equals("t")) {
            return new TickCommand(words);

        } else if (cmdWord.equals("info") || cmdWord.equals("i")) {
            return new InfoCommand(words);

        } else if (cmdWord.equals("build") || cmdWord.equals("b")) {
            return new BuildCommand(words);

        } else if (cmdWord.equals("help") || cmdWord.equals("?")) {
            return new HelpCommand(words);

        } else if (cmdWord.equals("cheat")) {
            return new CheatCommand(words);

        } else if (cmdWord.equals("save") || cmdWord.equals("s")) {
            return new SaveCommand(words);

        } else if (cmdWord.equals("load") || cmdWord.equals("l")) {
            return new LoadCommand(words);

        } else if (cmdWord.equals("graph") || cmdWord.equals("g")) {
            return new GraphCommand(words);

        } else if (cmdWord.equals("launch")) {
            return new LaunchCommand(words);

        } else if (cmdWord.equals("map")) {
            return new MapCommand(words);

        } else if (cmdWord.equals("move")) {
            return new MoveCommand(words);

        } else if (cmdWord.equals("explore")) {
            return new ExploreCommand(words);

        } else if (cmdWord.equals("assign")) {
            return new AssignCommand(words);

        } else if (cmdWord.equals("quit") || cmdWord.equals("q") || cmdWord.equals("exit")) {
            return new QuitCommand(words);

        } else {
            /*
                if none of the known commands match,
                we send back an InvalidCommand so the player is informed.
            */
            return new InvalidCommand(words);
        }
    }
}
