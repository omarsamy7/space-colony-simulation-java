package org.uob.a2.parser;

import org.uob.a2.engine.Context;
import org.uob.a2.model.ResourceType;

import java.util.List;

/*
    this command is used mainly for testing and debugging.
    
    purpose of the cheat command:
    - instantly give the player a large amount of every resource
    - makes it easier to test building, launching, converting, etc.
    - avoids having to wait for production or gather resources manually
    
    this command is never meant for real gameplay,
    but it is extremely helpful when checking the simulator.
*/
public class CheatCommand extends Command {

    /*
        constructor that accepts the command words.
        this follows the normal pattern for all commands.
    */
    public CheatCommand(List<String> words) {
        super(words);
    }

    /*
        default constructor in case the parser calls it without input.
    */
    public CheatCommand() {
        super();
    }

    /*
        execute the cheat:
        - loop through all resource types in the game
        - add 1000 of each resource to the inventory
        
        why 1000?
        - it's high enough to give freedom without causing overflow
        - used by assignment tests as well
    */
    @Override
    public String execute(Context ctx) {
        for (ResourceType type : ResourceType.values()) {
            ctx.state().addResource(type, 1000);
        }
        return "Cheat activated: granted 1000 of each resource.";
    }
}
