package org.uob.a2.parser;

import org.uob.a2.engine.Context;
import java.util.List;

public class MoveCommand extends Command {

    public MoveCommand(List<String> words) {
        super(words);
    }

    public MoveCommand() {
        super();
    }

    @Override
    public String execute(Context ctx) {
        if (words == null || words.size() < 2) {
            return "Usage: move <north|south|east|west>";
        }
        String dir = words.get(1).toLowerCase();
        boolean moved = ctx.state().movePlayer(dir);
        if (!moved) {
            return "You cannot move " + dir + " from here.";
        }
        return "Moved " + dir + ".";
    }
}
