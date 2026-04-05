package org.uob.a2.engine;

import org.uob.a2.model.ResourceType;

/*
    this is the base class for all producer-type entities in the simulation.
    a producer is something that generates resources every tick.
    examples in this project: solar farm (energy), mine (ore), hydroponic farm (food).

    why this class exists:
    - to hold common fields like what resource it produces and how much
    - to give all producers a shared structure
    - to require each producer to implement its own produce(...) logic

    the engine will call tick() on all producers every tick,
    and inside tick() the specific producer will call its produce method.
*/
public abstract class Producer extends Entity implements Tickable {

    // the type of resource this producer generates (energy, food, ore, etc.)
    protected ResourceType product;

    // how much resource is created per tick (baseline amount)
    protected int amount;

    /*
        constructor sets up the producer with:
        - a name (handled by entity)
        - what resource it produces
        - how much it produces per tick

        each specific producer can then modify or expand this logic.
    */
    public Producer(String name, ResourceType product, int amount) {
        super(name);
        this.product = product;
        this.amount = amount;
    }

    /*
        returns the resource type this producer outputs.
    */
    public ResourceType getProduct() {
        return product;
    }
    
    /*
        returns how much resource this producer generates per tick.
        this is the base amount; subclasses can add bonuses (like crew bonuses).
    */
    public int getAmount() {
        return amount;
    }
    
    /*
        each producer must implement how it produces resources.
        why it's abstract:
        - different producers have different production logic
        - allows producers to react to crew assignments, resource availability, etc.
    */
    public abstract void produce(Context ctx);

    /*
        returns a csv representation of the producer for saving.
        each producer type must define its own csv format.
    */
    public abstract String toCSV();

}
