package org.uob.a2.engine;

import org.uob.a2.*;
import org.uob.a2.model.*;

/*
    this is the base class for all consumer-type entities.
    a consumer is something in the simulation that uses up resources every tick.
    for example: the launchpad consumes resources when launching missions.

    this class:
    - stores which resource the consumer needs
    - stores how much it needs per tick (or per action)
    - forces child classes to implement their own consume logic

    the engine will call tick() on all consumers every simulation step.
*/
public abstract class Consumer extends Entity implements Tickable {

    // the resource this consumer uses (e.g. FUEL, FOOD, etc.)
    protected ResourceType consumedResource;

    // how much of that resource it consumes when triggered
    protected int amount;

    /*
        constructor sets up the consumer with a name, the resource it depends on,
        and how much of that resource it will consume when active.

        we call super(name) to set the entity name in the parent class.
    */
    public Consumer(String name, ResourceType resource, int amount) {
        super(name);
        this.consumedResource = resource;
        this.amount = amount;
    }

    /*
        get the resource type this consumer uses.
        even though it's called getProduct (for compatibility with the assignment),
        this actually returns the resource being consumed, not produced.
    */
    public ResourceType getProduct() {
        return consumedResource;
    }
    
    /*
        returns how much resource this consumer takes each time it activates.
        child classes can use this inside their consume(...) logic.
    */
    public int getAmount() {
        return amount;
    }

    /*
        each consumer must define how it consumes resources.
        this method will be called inside tick() based on the implementation.

        example: launchpad reduces METAL, FUEL and POPULATION when launching.
    */
    public abstract void consume(Context ctx);
}
