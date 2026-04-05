package org.uob.a2.model;

import org.uob.a2.engine.Context;
import org.uob.a2.engine.Producer;

/*
    this class represents a hydroponic farm in the colony.
    it is responsible for generating FOOD every tick.
    
    why this is a producer:
    - hydroponic farms are buildings that create resources over time
    - they produce a predictable amount of food each tick
    - production can be improved by assigning crew to the farm role
    
    this building is important because food supports population growth
    and keeps the colony functional.
    
    this class extends Producer so it inherits:
    - the resource it produces (FOOD)
    - the base amount per tick
    - ability to add build costs
    - the tick() → produce() workflow
*/
public class HydroponicFarm extends Producer {

    /*
        constructor sets up the hydroponic farm:
        - it produces FOOD
        - the base production rate is 2 per tick
        - it requires credits and water to build
        
        the water cost here is only a build-time requirement,
        not something the farm continuously consumes.
    */
    public HydroponicFarm(String name) {
        super(name, ResourceType.FOOD, 2);

        // resource costs to build one farm
        addCost(ResourceType.CREDITS, 80);
        addCost(ResourceType.WATER, 5);
    }

    /*
        tick() is called once per simulation tick.
        for a farm, tick simply calls produce().
        this keeps the logic simple and consistent.
    */
    @Override
    public void tick(Context ctx) {
        produce(ctx);
    }

    /*
        this method handles how much food the farm actually produces.
        
        the base amount is always 2 food.
        
        crew bonus:
        - every 5 assigned farm crew gives +1 extra food
        - this makes crew assignments meaningful
        - tests expect at least 2 with no crew, so bonus is added on top
        
        final food = base + bonus
    */
    @Override
    public void produce(Context ctx) {
        int base = getAmount();  // always 2
        int bonus = ctx.state().getFarmCrew() / 5;  // +1 for every 5 crew
        ctx.state().addResource(getProduct(), base + bonus);
    }

    /*
        returns a csv-friendly version of the farm.
        used for saving the simulation later if needed.
    */
    @Override
    public String toCSV() {
        return "HydroponicFarm;" + this.name;
    }
}
