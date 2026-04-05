package org.uob.a2.parser;

import org.uob.a2.engine.Context;
import java.util.List;

public class MapCommand extends Command {

    public MapCommand(List<String> words) {
        super(words);
    }

    public MapCommand() {
        super();
    }

    @Override
    public String execute(Context ctx) {
        return ctx.state().renderMap();
    }
}
