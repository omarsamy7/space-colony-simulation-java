package org.uob.a2.parser;

import org.uob.a2.engine.Context;
import java.util.List;

/*
    this command lets the player explore the tile they are currently standing on.
    
    purpose of exploring:
    - reveal the tile on the map
    - trigger a random event (bonus or penalty)
    - expand knowledge of the world as the colony grows
    
    exploring helps the player discover new resources and makes the map feel alive.
*/
public class ExploreCommand extends Command {

    /*
        standard constructor that accepts the split words
    */
    public ExploreCommand(List<String> words) {
        super(words);
    }

    /*
        empty constructor for flexibility
    */
    public ExploreCommand() {
        super();
    }

    /*
        execute explore:
        - simply calls the simulation state's explore method
        - the state handles unlocking, events, and tile marking
    */
    @Override
    public String execute(Context ctx) {
        return ctx.state().exploreCurrentTile();
    }
}
