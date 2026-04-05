package org.uob.a2.model;

import org.uob.a2.engine.Context;
import org.uob.a2.engine.Producer;

/*
    this class represents a mining facility in the colony.
    the job of a mine is to extract ORE every tick.
    
    it extends Producer because:
    - it generates resources over time
    - it follows the standard tick → produce workflow
    - it has a base production amount
    
    mines also benefit from crew assignments.
    assigning more miners increases the ore production,
    which makes crew management meaningful and strategic.
*/
public class Mine extends Producer {

    /*
        constructor sets up the mine:
        - base production: 3 ORE per tick
        - build cost: credits
        the Producer parent class handles most of the setup.
    */
    public Mine(String name) {
        super(name, ResourceType.ORE, 3);

        // building a mine costs credits
        addCost(ResourceType.CREDITS, 90);
    }

    /*
        tick() is called once per simulation step.
        for a mine, we simply call produce().
    */
    @Override
    public void tick(Context ctx) {
        produce(ctx);
    }

    /*
        production logic:
        - the mine always produces its base amount (3 ore)
        - crew bonus: every 5 miners = +1 extra ore
        this gives the player a reason to assign miners,
        and adds depth to resource management.
    */
    @Override
    public void produce(Context ctx) {
        int base = getAmount();  // always 3
        int bonus = ctx.state().getMineCrew() / 5;  // +1 ore for every 5 workers
        ctx.state().addResource(getProduct(), base + bonus);
    }

    /*
        returns a csv-friendly version of this entity.
        used later when saving the simulation.
    */
    @Override
    public String toCSV() {
        return "Mine;" + this.name;
    }
}
