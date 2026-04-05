package org.uob.a2.model;

import org.uob.a2.engine.Context;
import org.uob.a2.engine.Consumer;

/*
    this class represents the colony's launchpad.
    the launchpad is different from normal consumers or producers because:
    
    - it doesn't automatically consume resources each tick
    - instead, it performs a special "launch" action when the user calls the launch command
    - launching consumes resources and increases the launchpad level
    - when the level increases, the simulation unlocks more of the map
    
    so the launchpad is a consumer only in structure, but its real behaviour
    is controlled by a separate LaunchCommand.
    
    this design keeps the launch logic clear and user-triggered,
    instead of forcing it to happen automatically every tick.
*/
public class LaunchPad extends Consumer {

    // stores the current launchpad level (starts at level 1)
    private int level = 1;

    /*
        constructor sets up a launchpad with a name.
        
        since launching is not tied to automatic tick consumption,
        we pass ResourceType.FUEL and amount 0 to the parent class.
        this keeps the class compatible with the Consumer structure
        without forcing unwanted tick behaviour.
        
        we also define the build cost for constructing a launchpad.
    */
    public LaunchPad(String name) {
        super(name, ResourceType.FUEL, 0);

        // build costs for creating a launchpad in the colony
        addCost(ResourceType.CREDITS, 200);
        addCost(ResourceType.METAL, 20);
    }

    /*
        returns the current level of the launchpad.
        higher level = larger unlocked map.
    */
    public int getLevel() {
        return level;
    }

    /*
        allows the launchpad level to be set manually if needed.
        level cannot drop below 1.
    */
    public void setLevel(int level) {
        this.level = Math.max(1, level);
    }

    /*
        increases launchpad level by 1.
        this is called after a successful launch.
    */
    public void incrementLevel() {
        this.level++;
    }

    /*
        the launchpad does nothing automatically each tick.
        launching is intentionally handled by LaunchCommand,
        because launching is a player-triggered action, not a time-based one.
    */
    @Override
    public void tick(Context ctx) {
        // no passive behaviour here
    }

    /*
        normally consumers reduce resources here,
        but the launchpad uses LaunchCommand to handle resource usage.
        this method is left empty on purpose.
    */
    @Override
    public void consume(Context ctx) {
        // unused on purpose - actual launch logic is in LaunchCommand
    }

    /*
        csv format for saving launchpad state
        includes: type, name, and level
    */
    @Override
    public String toCSV() {
        return "LaunchPad;" + this.name + ";" + level;
    }
}
