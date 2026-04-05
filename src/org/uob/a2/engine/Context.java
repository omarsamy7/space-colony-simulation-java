package org.uob.a2.engine;

import org.uob.a2.engine.Engine;
import org.uob.a2.engine.SimulationState;

/*
    this class is used to group the engine and the simulation state together.
    the reason for this is to make it easy for commands and entities
    to access both the engine and the state without passing them separately.

    basically, a context = { engine + state }
    this is given to commands when they execute,
    and also to entities when they tick or produce/consume resources.

    keeping engine + state in one object makes the code cleaner
    and avoids having long method signatures everywhere.
*/
public class Context {

    // reference to the main engine (controls ticks, saving, loading)
    private final Engine engine;

    // reference to the simulation state (resources, map, entities, etc.)
    private final SimulationState state;

    /*
        constructor sets up the context with both engine and state.
        both are final because they should not change once the simulation starts.
    */
    public Context(Engine engine, SimulationState state) {
        this.engine = engine;
        this.state = state;
    }

    /*
        getter for the engine.
        commands call ctx.engine() to run ticks or save/load.
    */
    public Engine engine() { 
        return engine; 
    }

    /*
        getter for the simulation state.
        commands call ctx.state() to update resources, check map, etc.
    */
    public SimulationState state() { 
        return state; 
    }
}
