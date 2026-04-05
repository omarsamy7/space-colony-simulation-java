package org.uob.a2.parser;

import org.uob.a2.engine.Context;

import java.util.List;

public class HelpCommand extends Command {

    public HelpCommand(List<String> words) {
        super(words);
    }

    public HelpCommand() {
        super();
    }

    @Override
    public String execute(Context ctx) {
        if (words != null && words.size() >= 2) {
            String topic = words.get(1).toLowerCase();
            return helpFor(topic);
        }
        // General help
        return """
                Available commands:
                  tick|t               - Advance the simulation by one tick
                  info|i [resources|entities] - Show resource or entity information
                  build|b <type>       - Build an entity (solarfarm, hydroponicfarm, mine, etc.)
                  save|s <file>        - Save the simulation to a file
                  load|l <file>        - Load the simulation from a file
                  graph|g <resource>   - Show a text graph of a resource over time
                  launch <name>        - Launch from a LaunchPad consumer
                  map                  - Show the explored and locked areas of the colony
                  move <dir>           - Move on the map (north/south/east/west)
                  explore              - Explore the current tile for events/resources
                  assign <role> <n>    - Assign population to farm, mine or reactor work
                  cheat                - Add a large amount of each resource
                  quit|q               - Exit the simulation
                """.trim();
    }

    private String helpFor(String topic) {
        return switch (topic) {
            case "tick" -> "tick|t: Advance the simulation by one tick. All entities act once.";
            case "info" -> "info|i [resources|entities]: Show current resources or entities.";
            case "build" -> "build|b <type>: Build an entity (e.g. 'build solarfarm').";
            case "save" -> "save|s <file>: Save the simulation state to a file.";
            case "load" -> "load|l <file>: Load a previously saved simulation.";
            case "graph" -> "graph|g <resource>: Show a graph of a resource over time.";
            case "launch" -> "launch <name>: Use a LaunchPad to launch a mission and increase its level.";
            case "map" -> "map: Display a 10x10 map showing your position (P), explored tiles (.), unexplored tiles (?), and locked areas (#).";
            case "move" -> "move <north|south|east|west>: Move your position on the map within the unlocked area.";
            case "explore" -> "explore: Explore your current tile and trigger a random event (e.g. find resources or encounter a storm).";
            case "assign" -> "assign <farm|mine|reactor> <n>: Assign n units of population to that job, improving production if enough crew are assigned.";
            case "cheat" -> "cheat: Grant a large amount of every resource for testing.";
            case "quit", "q" -> "quit|q: Exit the simulation.";
            default -> "No detailed help available for '" + topic + "'.";
        };
    }
}
