package org.uob.a2.parser;

import org.uob.a2.engine.Context;
import org.uob.a2.engine.Consumer;
import org.uob.a2.engine.SimulationState;
import org.uob.a2.model.LaunchPad;
import org.uob.a2.model.ResourceType;

import java.util.List;

/*
    this command lets the player launch a mission using one of their launchpads.

    what launching means:
    - consumes METAL, FUEL, and POPULATION
    - costs increase depending on the launchpad's current level
    - if the player has enough resources, the mission succeeds
    - the launchpad levels up after each successful mission
    - higher level unlocks more of the map

    why this command is important:
    - launching drives progression
    - unlocking more of the map allows more exploration and events
    - leveling launchpads creates long-term goals for the player
*/
public class LaunchCommand extends Command {

    /*
        constructor when parser provides arguments
    */
    public LaunchCommand(List<String> words) {
        super(words);
    }

    /*
        empty constructor for flexibility
    */
    public LaunchCommand() {
        super();
    }

    /*
        execute the launch logic:
        - checks arguments
        - finds the correct launchpad
        - calculates resource costs based on level
        - checks affordability
        - subtracts resources
        - updates crew limits and unlock radius
        - levels up the launchpad
    */
    @Override
    public String execute(Context ctx) {

        // user must specify which launchpad to use
        if (words == null || words.size() < 2) {
            return "Usage: launch <launchpad-name>";
        }

        String targetName = words.get(1);
        SimulationState state = ctx.state();

        // find the correct launchpad in the colony
        LaunchPad lp = findLaunchPad(state, targetName);
        if (lp == null) {
            return "No LaunchPad found with name '" + targetName + "'.";
        }

        // costs scale with launchpad level
        int level = lp.getLevel();
        int metalCost = 10 * level;
        int fuelCost = 10 * level;
        int popCost = 5 * level;

        // check all required resources
        if (state.getResourceAmount(ResourceType.METAL) < metalCost) {
            return "Not enough METAL to launch. Need " + metalCost + ".";
        }
        if (state.getResourceAmount(ResourceType.FUEL) < fuelCost) {
            return "Not enough FUEL to launch. Need " + fuelCost + ".";
        }
        if (state.getResourceAmount(ResourceType.POPULATION) < popCost) {
            return "Not enough POPULATION to crew this mission. Need " + popCost + ".";
        }

        /*
            pay the resource cost:
            - remove metal
            - remove fuel
            - remove population
        */
        state.removeResource(ResourceType.METAL, metalCost);
        state.removeResource(ResourceType.FUEL, fuelCost);
        state.removeResource(ResourceType.POPULATION, popCost);

        // make sure crew roles do not exceed the new population amount
        state.clampCrewToPopulation();

        // increase launchpad level after successful mission
        lp.incrementLevel();
        int newLevel = lp.getLevel();

        // increase how much of the map is unlocked
        ctx.state().updateUnlockedRadiusForLevel(newLevel);

        return "Launch mission successful! " + lp.getName() +
               " is now level " + newLevel + ".";
    }

    /*
        helper method to find a launchpad by name
        - loops through all consumers
        - checks for launchpad instances
        - compares names ignoring case
    */
    private LaunchPad findLaunchPad(SimulationState state, String name) {
        for (Consumer cons : state.getConsumers()) {
            if (cons instanceof LaunchPad lp) {
                if (lp.getName().equalsIgnoreCase(name)) {
                    return lp;
                }
            }
        }
        return null;
    }
}
