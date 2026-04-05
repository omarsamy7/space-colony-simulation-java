package org.uob.a2.parser;

import org.uob.a2.*;
import org.uob.a2.engine.*;
import java.util.List;

/*
    this is the base class for every command in the simulation.
    
    what a command represents:
    - a command is an action the player can perform (tick, build, info, etc.)
    - each command is created by the Parser based on user input
    - then Main calls cmd.execute(ctx) to run it

    why this class exists:
    - to provide a shared structure for all commands
    - so every command stores the original words the user typed
    - to force all commands to implement execute()
    
    every custom command (BuildCommand, InfoCommand, MoveCommand...) extends this.
*/
public abstract class Command {

    // list of words the user typed (split by spaces)
    // example: "build solarfarm" -> ["build", "solarfarm"]
    protected List<String> words;

    /*
        constructor that accepts the parsed words.
        this allows each command to access arguments if needed.
    */
    public Command(List<String> words) {
        this.words = words;
    }

    /*
        empty constructor for commands that don't need arguments.
        some commands (e.g., quit or cheat) may use this.
    */
    public Command() {}

    /*
        every command must define what happens when it executes.
        the context gives access to both the engine and the simulation state.

        execute(...) returns a string that gets printed in Main.
    */
    public abstract String execute(Context ctx);
}
