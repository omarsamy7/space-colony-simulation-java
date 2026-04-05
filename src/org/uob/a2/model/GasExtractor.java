package org.uob.a2.model;

import org.uob.a2.engine.Context;
import org.uob.a2.engine.Producer;

/*
    this class represents a gas extractor building.
    it is a simple producer that generates GAS every tick.
    all production logic is handled by the parent Producer class,
    and this class only defines the specific details:

    - how much gas it produces
    - how much it costs to build
    - how it saves itself in csv format

    this keeps the code clean and each producer type easy to understand.
*/
public class GasExtractor extends Producer {

    /*
        constructor sets up the gas extractor.
        it produces 2 units of GAS per tick.
        it also has a credit cost, so the player must spend resources to build it.
    */
    public GasExtractor(String name) {
        // tell the producer it makes GAS and produces 2 each tick
        super(name, ResourceType.GAS, 2);

        // cost to build one gas extractor
        addCost(ResourceType.CREDITS, 70);
    }

    /*
        tick() is called once per simulation tick.
        here we simply call produce(), since gas extraction has no special logic.
    */
    @Override
    public void tick(Context ctx) {
        produce(ctx);
    }

    /*
        produce gas and add it to the simulation state.
        this uses getProduct() and getAmount() inherited from Producer.
    */
    @Override
    public void produce(Context ctx) {
        ctx.state().addResource(getProduct(), getAmount());
    }

    /*
        returns a csv-friendly format of the entity.
        used later when saving entities to a file.
    */
    @Override
    public String toCSV() {
        return "GasExtractor;" + this.name;
    }
}
