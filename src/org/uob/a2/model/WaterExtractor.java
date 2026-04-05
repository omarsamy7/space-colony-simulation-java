package org.uob.a2.model;

import org.uob.a2.engine.Context;
import org.uob.a2.engine.Producer;

/*
    this class represents a water extractor in the colony.
    
    purpose of this building:
    - generate WATER every tick
    - water is one of the essential resources for farming and building costs
    
    this producer is simple:
    - creates a fixed amount of water each tick
    - has a credit cost to build
    - does not require crew or any special conditions
    
    it extends Producer because it generates resources over time.
*/
public class WaterExtractor extends Producer {

    /*
        constructor defines:
        - resource produced: WATER
        - production amount: 2 per tick
        - build cost: credits
    */
    public WaterExtractor(String name) {
        super(name, ResourceType.WATER, 2);

        // building a water extractor costs credits
        addCost(ResourceType.CREDITS, 60);
    }

    /*
        tick() runs every simulation tick.
        water extractors simply call produce() because their production
        is straightforward and requires no special logic.
    */
    @Override
    public void tick(Context ctx) {
        produce(ctx);
    }

    /*
        production logic:
        - always adds 2 water to the simulation state
        - no crew bonuses
        - no input requirements
    */
    @Override
    public void produce(Context ctx) {
        ctx.state().addResource(getProduct(), getAmount());
    }

    /*
        returns a csv-friendly string used for saving.
    */
    @Override
    public String toCSV() {
        return "WaterExtractor;" + this.name;
    }
}
