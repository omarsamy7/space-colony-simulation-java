package org.uob.a2.engine;

import org.uob.a2.model.*;

import java.util.EnumMap;
import java.util.Map;

/*
    this is the base class for every type of entity in the simulation.
    all producers, converters, consumers, and any future structures
    will extend from this class.

    why this class exists:
    - to store common fields shared by all entities (like name and build costs)
    - to avoid repeating code in every entity subclass
    - to provide a standard format for saving entities using toCSV()

    this class does not implement tick() or any behaviour,
    because that depends on the specific type of entity (producer, converter, etc.).
*/
public abstract class Entity {

    // the name of this entity (example: "SolarFarm-1")
    protected String name;

    // the cost of building this entity, stored as resource -> amount
    // using EnumMap because it's fast and works perfectly with enums
    protected Map<ResourceType, Integer> costs = new EnumMap<>(ResourceType.class);

    // this variable isn't directly used but kept for compatibility with assignment structure
    protected int costAmount;

    /*
        constructor sets up the entity with a name.
        every entity must have a name to identify it in the simulation.
    */
    public Entity(String name) {
        this.name = name;
    }

    /*
        returns the name of the entity.
    */
    public String getName() {
        return this.name;
    }

    /*
        allows renaming the entity if needed.
        useful if user wants custom names or saving/loading uses different formats.
    */
    public void setName(String name) {
        this.name = name;
    }

    /*
        adds a build cost for this entity.
        why:
        - when the user builds something, the simulation checks these costs
        - subtracts required resources from inventory
        - prevents building things for free

        example: addCost(ResourceType.METAL, 20)
    */
    public void addCost(ResourceType resource, int amount) {
        costs.put(resource, amount);
    }

    /*
        returns all build costs for this entity.
        used by the build command to check affordability.
    */
    public Map<ResourceType, Integer> getCosts() {
        return costs;
    }

    /*
        every entity must provide a csv string for saving.
        this lets us store entities in save files easily later.

        each specific entity type (like SolarFarm, Reactor, LaunchPad)
        will implement this in its own way.
    */
    public abstract String toCSV();
   
}
