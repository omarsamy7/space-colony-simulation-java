package org.uob.a2.parser;

import org.uob.a2.engine.Context;
import java.util.List;

public class QuitCommand extends Command {

    public QuitCommand(List<String> words) {
        super(words);
    }

    public QuitCommand() {
        super();
    }

    @Override
    public String execute(Context ctx) {
        return "Quitting simulation.";
    }
}
