package org.uob.a2.model;

import org.uob.a2.engine.Context;
import org.uob.a2.engine.Producer;

/*
    this class represents a solar farm in the colony.
    
    purpose of a solar farm:
    - generate ENERGY each tick
    - energy is one of the most important resources in the colony
      because many buildings or events require it
    
    this building is very simple:
    - no input required to produce energy
    - always produces the same amount every tick
    - build cost is credits
    
    it extends Producer because a solar farm is a resource generator.
*/
public class SolarFarm extends Producer {

    /*
        constructor defines:
        - resource produced: ENERGY
        - production amount: 5 per tick
        - the cost to build this structure
    */
    public SolarFarm(String name) {
        super(name, ResourceType.ENERGY, 5);

        // the player must spend 100 credits to build a solar farm
        addCost(ResourceType.CREDITS, 100);
    }

    /*
        tick() is called every simulation step.
        solar farms just call produce() because their production is simple.
    */
    @Override
    public void tick(Context ctx) {
        produce(ctx);
    }

    /*
        production logic:
        - no special conditions
        - no crew bonuses (kept simple)
        - always adds 5 energy per tick
        
        this is the basic energy provider for the colony.
    */
    @Override
    public void produce(Context ctx) {
        ctx.state().addResource(getProduct(), getAmount());
    }

    /*
        returns a csv version of this entity for saving.
        format: SolarFarm;<name>
    */
    @Override
    public String toCSV() {
        return "SolarFarm;" + this.name;
    }
}
