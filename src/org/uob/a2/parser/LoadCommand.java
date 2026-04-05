package org.uob.a2.parser;

import org.uob.a2.engine.Context;

import java.util.List;

/*
    this command loads a previously saved simulation file.
    
    what the load command does:
    - it takes a filename from the user
    - asks the engine to load the saved simulation data
    - returns whatever message the engine gives back (success or error)
    
    why this is useful:
    - lets the player continue a previous session
    - important for testing long-term behaviour of the colony
*/
public class LoadCommand extends Command {

    /*
        constructor that receives the parsed user words.
        this follows the normal pattern for all commands.
    */
    public LoadCommand(List<String> words) {
        super(words);
    }

    /*
        empty constructor for flexibility.
        some commands can be created with or without arguments.
    */
    public LoadCommand() {
        super();
    }

    /*
        executes the load operation.
        steps:
        1. check that the user typed a filename
        2. pass that filename to the engine's load method
        3. return the engine's response message
    */
    @Override
    public String execute(Context ctx) {

        // must have a filename after "load"
        if (words == null || words.size() < 2) {
            return "Usage: load <filename>";
        }

        String filename = words.get(1);

        // ask the engine to load the file
        return ctx.engine().load(filename);
    }
}
