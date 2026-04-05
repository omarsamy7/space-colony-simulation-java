package org.uob.a2.parser;

import org.uob.a2.engine.Context;
import org.uob.a2.engine.Producer;
import org.uob.a2.engine.Converter;
import org.uob.a2.engine.Consumer;
import org.uob.a2.engine.SimulationState;
import org.uob.a2.model.*;

import java.util.List;
import java.util.Map;

/*
    this command is responsible for building new entities in the colony.

    what building means:
    - check if the player typed a valid entity name
    - check if the player has enough resources to build it
    - create the correct entity object
    - subtract the cost from the inventory
    - add the new entity to the correct list (producer, converter, consumer)

    purpose of this command:
    - lets the player expand the colony
    - unlocks more production, conversion, fuel generation, and launching ability
    - ensures resources are properly checked and deducted
*/
public class BuildCommand extends Command {

    /*
        constructor used when parser provides arguments
    */
    public BuildCommand(List<String> words) {
        super(words);
    }

    /*
        empty constructor for flexibility
    */
    public BuildCommand() {
        super();
    }

    /*
        main execute logic:
        - requires at least one argument after "build"
        - tries to create the correct entity
        - checks affordability
        - pays the cost
        - adds the entity to the simulation
    */
    @Override
    public String execute(Context ctx) {

        // user must specify which building they want
        if (words == null || words.size() < 2) {
            return "Usage: build <entityName>";
        }

        String typeName = words.get(1).toLowerCase();
        SimulationState state = ctx.state();

        // try to build the entity
        EntityWithType result = createEntity(typeName, state);
        if (result == null) {
            return "Unknown entity type: " + words.get(1);
        }

        // check if player has enough resources
        if (!canAfford(state, result.entity)) {
            return "Not enough resources to build " + result.entity.getName() + ".";
        }

        // subtract the cost from the inventory
        payCost(state, result.entity);

        // add the new entity to its correct category
        switch (result.kind) {
            case PRODUCER ->
                state.addProducer((Producer) result.entity);

            case CONVERTER ->
                state.addConverter((Converter) result.entity);

            case CONSUMER ->
                state.addConsumer((Consumer) result.entity);
        }

        return "Built " + result.entity.getName() + ".";
    }

    /*
        checks whether the player can afford an entity:
        - loops through all cost entries
        - ensures inventory has enough of each one
    */
    private boolean canAfford(SimulationState state, org.uob.a2.engine.Entity e) {
        for (Map.Entry<ResourceType, Integer> entry : e.getCosts().entrySet()) {
            if (state.getResourceAmount(entry.getKey()) < entry.getValue()) {
                return false;
            }
        }
        return true;
    }

    /*
        subtracts required resources after building
    */
    private void payCost(SimulationState state, org.uob.a2.engine.Entity e) {
        for (Map.Entry<ResourceType, Integer> entry : e.getCosts().entrySet()) {
            state.removeResource(entry.getKey(), entry.getValue());
        }
    }

    /*
        creates an entity object based on the name typed by the player.
        this also auto-increments the number (e.g. SolarFarm-1, SolarFarm-2)
        based on how many already exist.

        returns:
        - an object containing the entity
        - the type (producer, converter, consumer)
    */
    private EntityWithType createEntity(String typeName, SimulationState state) {
        int index;

        switch (typeName) {
            case "solarfarm":
                index = countByPrefix(state, "SolarFarm-") + 1;
                return new EntityWithType(new SolarFarm("SolarFarm-" + index), Kind.PRODUCER);

            case "hydroponicfarm":
                index = countByPrefix(state, "HydroponicFarm-") + 1;
                return new EntityWithType(new HydroponicFarm("HydroponicFarm-" + index), Kind.PRODUCER);

            case "mine":
                index = countByPrefix(state, "Mine-") + 1;
                return new EntityWithType(new Mine("Mine-" + index), Kind.PRODUCER);

            case "gasextractor":
                index = countByPrefix(state, "GasExtractor-") + 1;
                return new EntityWithType(new GasExtractor("GasExtractor-" + index), Kind.PRODUCER);

            case "waterextractor":
                index = countByPrefix(state, "WaterExtractor-") + 1;
                return new EntityWithType(new WaterExtractor("WaterExtractor-" + index), Kind.PRODUCER);

            case "house":
                index = countByPrefix(state, "House-") + 1;
                return new EntityWithType(new House("House-" + index), Kind.PRODUCER);

            case "smelter":
                index = countByPrefix(state, "Smelter-") + 1;
                return new EntityWithType(new Smelter("Smelter-" + index), Kind.CONVERTER);

            case "reactor":
                index = countByPrefix(state, "Reactor-") + 1;
                return new EntityWithType(new Reactor("Reactor-" + index), Kind.CONVERTER);

            case "launchpad":
                index = countByPrefix(state, "LaunchPad-") + 1;
                return new EntityWithType(new LaunchPad("LaunchPad-" + index), Kind.CONSUMER);

            default:
                return null;
        }
    }

    /*
        counts how many existing entities start with a certain prefix.
        this is how we generate names like:
        SolarFarm-1, SolarFarm-2, SolarFarm-3...
    */
    private int countByPrefix(SimulationState state, String prefix) {
        int count = 0;

        for (Producer p : state.getProducers()) {
            if (p.getName().startsWith(prefix)) count++;
        }
        for (Converter c : state.getConverters()) {
            if (c.getName().startsWith(prefix)) count++;
        }
        for (Consumer cons : state.getConsumers()) {
            if (cons.getName().startsWith(prefix)) count++;
        }

        return count;
    }

    /*
        enum to mark what type of entity we are dealing with
    */
    private enum Kind { PRODUCER, CONVERTER, CONSUMER }

    /*
        wrapper class to hold:
        - the created entity
        - the entity type (for placing it in the right list)
    */
    private static class EntityWithType {
        final org.uob.a2.engine.Entity entity;
        final Kind kind;

        EntityWithType(org.uob.a2.engine.Entity entity, Kind kind) {
            this.entity = entity;
            this.kind = kind;
        }
    }
}
