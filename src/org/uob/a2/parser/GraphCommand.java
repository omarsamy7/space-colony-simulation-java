package org.uob.a2.parser;

import org.uob.a2.engine.Context;
import org.uob.a2.model.ResourceType;

import java.util.List;
import java.util.Map;

/*
    this command draws a simple text-based graph of a resource over time.
    
    purpose of the graph command:
    - let the player visualise how a resource changed across ticks
    - useful for understanding production, consumption, and trends
    - helps debug the simulation by showing resource patterns

    how it works:
    1. read the resource name from the user
    2. get the resource history from the simulation state
    3. scale the values so the graph fits nicely
    4. print stars (*) to represent the amount at each tick

    the graph is kept simple so it works well in a console window.
*/
public class GraphCommand extends Command {

    /*
        normal constructor that receives the parsed command words.
    */
    public GraphCommand(List<String> words) {
        super(words);
    }

    /*
        empty constructor for flexibility.
    */
    public GraphCommand() {
        super();
    }

    /*
        executes the command and returns the final graph as a string.
    */
    @Override
    public String execute(Context ctx) {

        // make sure the user actually typed a resource name
        if (words == null || words.size() < 2) {
            return "Usage: graph <resource>";
        }

        // convert the resource name to uppercase (matches enum formatting)
        String resourceName = words.get(1).toUpperCase();

        // try to match the resource name with an enum value
        ResourceType type;
        try {
            type = ResourceType.valueOf(resourceName);
        } catch (IllegalArgumentException e) {
            return "Unknown resource '" + words.get(1) + "'.";
        }

        // get the full resource history (one entry per tick)
        var history = ctx.state().getResourceHistory();
        if (history.isEmpty()) {
            return "No history available yet. Try running some ticks first.";
        }

        // find the highest value so we can scale the graph properly
        int max = 0;
        for (Map<ResourceType, Integer> snapshot : history) {
            int val = snapshot.getOrDefault(type, 0);
            if (val > max) {
                max = val;
            }
        }

        // if max is zero, there's nothing meaningful to graph
        if (max == 0) {
            return "All values for " + type.name() + " are zero.";
        }

        // scaling so the graph does not become too wide
        int scale = Math.max(1, max / 50);

        // build the graph output
        StringBuilder sb = new StringBuilder("Graph for " + type.name() + ":\n");

        for (int i = 0; i < history.size(); i++) {
            int val = history.get(i).getOrDefault(type, 0);
            int count = val / scale;

            // tick number formatting: aligns numbers neatly
            sb.append(String.format("Tick %3d: ", i + 1));

            // draw the stars representing this value
            for (int j = 0; j < count; j++) {
                sb.append('*');
            }

            // show the actual value beside the graph bar
            sb.append(" (").append(val).append(")\n");
        }

        // remove trailing newline before returning
        return sb.toString().trim();
    }
}
