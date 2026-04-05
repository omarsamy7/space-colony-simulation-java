package org.uob.a2.parser;

import org.uob.a2.engine.Context;

import java.util.List;

/*
    this command saves the current state of the simulation to a file.
    
    what the save command does:
    - it reads the filename the user typed
    - it asks the engine to save the world into that file
    - it returns the engine’s confirmation or error message

    why this is useful:
    - allows the player to pause and continue later
    - important for long simulations or testing
*/
public class SaveCommand extends Command {

    /*
        standard constructor that receives the parsed words.
        almost all commands use a similar pattern.
    */
    public SaveCommand(List<String> words) {
        super(words);
    }

    /*
        empty constructor, used if the parser decides this command
        doesn't require initial arguments.
    */
    public SaveCommand() {
        super();
    }

    /*
        execute logic:
        1. check that the user provided a filename
        2. call the engine.save() function
        3. return its output so Main can print it
    */
    @Override
    public String execute(Context ctx) {

        // must have something after "save"
        if (words == null || words.size() < 2) {
            return "Usage: save <filename>";
        }

        String filename = words.get(1);

        // call the engine to save the simulation
        return ctx.engine().save(filename);
    }
}
