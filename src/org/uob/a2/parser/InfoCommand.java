package org.uob.a2.parser;

import org.uob.a2.engine.Context;
import org.uob.a2.engine.Producer;
import org.uob.a2.engine.Converter;
import org.uob.a2.engine.Consumer;
import org.uob.a2.model.ResourceType;

import java.util.List;

/*
    this command displays useful information to the player.

    what info command shows:
    - all current resources and their amounts
    - all existing entities in the simulation (producers, converters, consumers)

    how it works:
    - if the player types "info resources", only resources are shown
    - if the player types "info entities", only entities are shown
    - if the player types just "info", both sections are shown

    this command helps the player understand the current state of the colony,
    which is important for decision-making (building, launching, assigning crew, etc.)
*/
public class InfoCommand extends Command {

    /*
        normal constructor that receives the parsed command words.
    */
    public InfoCommand(List<String> words) {
        super(words);
    }

    /*
        empty constructor for flexibility.
    */
    public InfoCommand() {
        super();
    }

    /*
        main execution:
        - checks if the user wants "resources" or "entities"
        - defaults to showing everything if no specific topic is provided
    */
    @Override
    public String execute(Context ctx) {

        // check if a topic was specified
        if (words != null && words.size() >= 2) {
            String topic = words.get(1).toLowerCase();

            if (topic.equals("resources")) {
                return resourcesInfo(ctx);
            } 
            else if (topic.equals("entities")) {
                return entitiesInfo(ctx);
            }
        }

        // default: show both sections
        return resourcesInfo(ctx) + "\n" + entitiesInfo(ctx);
    }

    /*
        builds the resource info section.
        loops through all resource types and prints their current values.
    */
    private String resourcesInfo(Context ctx) {
        StringBuilder sb = new StringBuilder("Resources:\n");

        for (ResourceType type : ResourceType.values()) {
            int amount = ctx.state().getResourceAmount(type);
            sb.append(" - ")
              .append(type.name())
              .append(": ")
              .append(amount)
              .append("\n");
        }

        return sb.toString().trim();
    }

    /*
        builds the entities info section.
        shows the names of all producers, converters, and consumers.
    */
    private String entitiesInfo(Context ctx) {
        StringBuilder sb = new StringBuilder("Entities:\n");

        sb.append("Producers:\n");
        for (Producer p : ctx.state().getProducers()) {
            sb.append(" - ").append(p.getName()).append("\n");
        }

        sb.append("Converters:\n");
        for (Converter c : ctx.state().getConverters()) {
            sb.append(" - ").append(c.getName()).append("\n");
        }

        sb.append("Consumers:\n");
        for (Consumer cons : ctx.state().getConsumers()) {
            sb.append(" - ").append(cons.getName()).append("\n");
        }

        return sb.toString().trim();
    }
}
