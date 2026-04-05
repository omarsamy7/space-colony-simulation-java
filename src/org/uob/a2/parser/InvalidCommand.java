package org.uob.a2.parser;

import org.uob.a2.engine.Context;
import java.util.List;

public class InvalidCommand extends Command {

    public InvalidCommand(List<String> words) {
        super(words);
    }

    public InvalidCommand() {
        super();
    }

    @Override
    public String execute(Context ctx) {
        return "I did not understand that command.";
    }
}
