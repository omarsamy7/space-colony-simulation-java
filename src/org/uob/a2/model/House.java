package org.uob.a2.model;

import org.uob.a2.engine.Context;
import org.uob.a2.engine.Producer;

/*
    this class represents a house structure in the colony.
    the purpose of a house is to increase the colony population over time.

    it works as a producer because:
    - every tick it generates POPULATION
    - population is used for crew assignments (farm, mine, reactor)

    the house is simple and does not require any special conditions to produce.
    it only adds population each tick and has its own build costs.
*/
public class House extends Producer {

    /*
        constructor defines:
        - the name of the house
        - the resource it produces (POPULATION)
        - how much it produces each tick (1)
        - the cost to build it (credits, metal, food)

        the parent Producer class handles most of the storage.
    */
    public House(String name) {
        // produce 1 POPULATION per tick
        super(name, ResourceType.POPULATION, 1);

        // the building costs for constructing a house
        addCost(ResourceType.CREDITS, 50);
        addCost(ResourceType.METAL, 5);
        addCost(ResourceType.FOOD, 5);
    }

    /*
        tick method is called every simulation tick.
        houses have no special behaviour, so we simply call produce().
    */
    @Override
    public void tick(Context ctx) {
        produce(ctx);
    }

    /*
        adds population to the simulation state.
        this uses the standard producer behaviour:
        getProduct() -> POPULATION
        getAmount() -> 1
    */
    @Override
    public void produce(Context ctx) {
        ctx.state().addResource(getProduct(), getAmount());
    }

    /*
        returns a csv-friendly format for saving this entity.
    */
    @Override
    public String toCSV() {
        return "House;" + this.name;
    }
}
