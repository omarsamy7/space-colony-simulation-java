package org.uob.a2.model;

import org.uob.a2.engine.Context;
import org.uob.a2.engine.Converter;

/*
    this class represents a smelter in the colony.
    
    purpose of the smelter:
    - convert raw ORE into refined METAL
    - this is important because many advanced buildings require METAL
    - without the smelter, the colony cannot progress technologically

    it extends Converter because:
    - it takes an input resource (ORE)
    - it produces an output resource (METAL)
    - it does this based on fixed input/output ratios
*/
public class Smelter extends Converter {

    /*
        constructor defines the smelter’s conversion recipe:
        - input: 5 ORE
        - output: 1 METAL
        and also sets its build cost.
    */
    public Smelter(String name) {
        super(name, ResourceType.ORE, 5, ResourceType.METAL, 1);

        // building a smelter costs credits
        addCost(ResourceType.CREDITS, 120);
    }

    /*
        tick() is called once per simulation step.
        smelters simply call convert() because their whole purpose is conversion.
    */
    @Override
    public void tick(Context ctx) {
        convert(ctx);
    }

    /*
        conversion logic:
        - check if there is enough ORE to convert
        - if yes, remove 5 ORE and add 1 METAL
        - if not enough ore, nothing happens
        
        removeResource() already returns false if there is not enough,
        so the if statement neatly handles this case.
    */
    @Override
    public void convert(Context ctx) {
        if (ctx.state().removeResource(getInput(), getInputAmount())) {
            ctx.state().addResource(getOutput(), getOutputAmount());
        }
    }

    /*
        returns a csv version of the smelter.
        used later when saving the simulation.
    */
    @Override
    public String toCSV() {
        return "Smelter;" + this.name;
    }
}
