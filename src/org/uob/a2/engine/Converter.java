package org.uob.a2.engine;

import org.uob.a2.model.ResourceType;

/*
    this is the base class for all converter-type entities.

    a converter takes one resource (the input) and turns it into another resource (the output).
    examples from your simulation:
    - smelter: converts ORE into METAL
    - reactor: converts GAS into FUEL

    this class stores:
    - the input resource type
    - the output resource type
    - how much input is required
    - how much output is produced

    each specific converter (smelter, reactor, etc.) must implement the convert() method
    to define its own behaviour, and the engine will call tick() on them each simulation step.
*/
public abstract class Converter extends Entity implements Tickable {

    // the resource required for conversion (e.g., ORE or GAS)
    protected ResourceType input;

    // the resource produced after conversion (e.g., METAL or FUEL)
    protected ResourceType output;

    // how much of the input resource is consumed per tick
    protected int inputAmount;

    // how much output resource is produced per tick
    protected int outputAmount;

    /*
        constructor sets up the converter with its name, input/output resource types,
        and the amounts needed/produced.

        we call super(name) to set the entity name from the parent class.
    */
    public Converter(String name, ResourceType input, int inputAmount, ResourceType output, int outputAmount) {
        super(name);
        this.input = input;
        this.output = output;
        this.inputAmount = inputAmount;
        this.outputAmount = outputAmount;
    }

    /*
        return the resource type needed to perform the conversion.
    */
    public ResourceType getInput() {
        return input;
    }

    /*
        return the resource type that this converter produces.
    */
    public ResourceType getOutput() {
        return output;
    }

    /*
        return how much of the input resource is needed per conversion.
    */
    public int getInputAmount() {
        return inputAmount;
    }

    /*
        return how much of the output resource is produced per conversion.
    */
    public int getOutputAmount() {
        return outputAmount;
    }

    /*
        each converter must implement its own logic for converting resources.

        this method is called inside tick() by the engine.
        typical logic:
        - check if there is enough input resource
        - if yes, remove input and add output
        - if not, do nothing (or you can add your own behaviour)
    */
    public abstract void convert(Context ctx);
}
