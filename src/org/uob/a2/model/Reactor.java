package org.uob.a2.model;

import org.uob.a2.engine.Context;
import org.uob.a2.engine.Converter;

/*
    this class represents a fuel reactor inside the colony.
    
    its job:
    - take in METAL and GAS
    - convert them into FUEL
    
    this makes reactor a converter (input → output), not a producer.
    
    why this class is important:
    - fuel is needed for launching missions
    - this building lets the colony turn raw materials into advanced resources
    - crew can be assigned to improve efficiency (higher fuel output)
*/
public class Reactor extends Converter {

    // the reactor always needs 3 gas to run its conversion process
    private static final int GAS_REQUIRED = 3;

    /*
        constructor defines:
        - converter input (1 metal)
        - converter output (1 fuel)
        - build costs for constructing a reactor
        
        the Converter parent class stores:
        - input resource
        - output resource
        - input amount
        - output amount
    */
    public Reactor(String name) {
        super(name, ResourceType.METAL, 1, ResourceType.FUEL, 1);

        // cost to build one reactor
        addCost(ResourceType.CREDITS, 150);
        addCost(ResourceType.METAL, 10);
    }

    /*
        tick() is called once per simulation tick.
        reactors don't produce automatically; they convert resources instead,
        so we simply call convert().
    */
    @Override
    public void tick(Context ctx) {
        convert(ctx);
    }

    /*
        conversion logic:
        - must have at least 1 METAL
        - must have at least 3 GAS
        - if both conditions are met, remove those resources
        - then add the produced fuel
        
        crew bonus:
        - for every 5 workers assigned to reactor crew → +1 extra fuel
        - this makes assigning crew meaningful
    */
    @Override
    public void convert(Context ctx) {

        // check if we have enough required resources
        boolean hasMetal = ctx.state().getResourceAmount(ResourceType.METAL) >= 1;
        boolean hasGas   = ctx.state().getResourceAmount(ResourceType.GAS) >= GAS_REQUIRED;

        if (hasMetal && hasGas) {

            // remove input resources
            ctx.state().removeResource(ResourceType.METAL, 1);
            ctx.state().removeResource(ResourceType.GAS, GAS_REQUIRED);

            // calculate fuel output
            int base = getOutputAmount();  // base amount from converter (1)
            int bonus = ctx.state().getReactorCrew() / 5;  // +1 fuel per 5 crew

            ctx.state().addResource(ResourceType.FUEL, base + bonus);
        }
    }

    /*
        returns csv-friendly format for saving reactor state.
    */
    @Override
    public String toCSV() {
        return "Reactor;" + this.name;
    }
}
