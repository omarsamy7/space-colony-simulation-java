package org.uob.a2.parser;

import org.uob.a2.engine.Context;
import java.util.List;

/*
    this command advances the simulation by one tick.

    what a tick means:
    - all producers generate resources
    - all converters process input into output
    - all consumers perform their actions
    - the engine updates resource history for graphing
    - time essentially moves forward in the colony

    why this command exists:
    - ticking is the main way the player makes progress
    - most resources and events depend on ticks
    - separating it into its own command keeps the design clean
*/
public class TickCommand extends Command {

    /*
        constructor that accepts the parsed words.
        tick usually has no extra arguments, but we keep the structure consistent.
    */
    public TickCommand(List<String> words) {
        super(words);
    }

    /*
        empty constructor in case parser creates this command without arguments.
    */
    public TickCommand() {
        super();
    }

    /*
        execute the command:
        simply tells the engine to advance one tick
        and returns whatever message the engine provides.
    */
    @Override
    public String execute(Context ctx) {
        return ctx.engine().nextTick();
    }
}
 