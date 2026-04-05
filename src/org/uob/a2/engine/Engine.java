package org.uob.a2.engine;

import org.uob.a2.model.*;   // includes ResourceType and all your entities

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

/*
    the engine is the main controller of the simulation.
    its job is to:
    - keep track of the current tick (time step)
    - trigger all producers, converters, and consumers every tick
    - manage saving and loading game files
    - provide access to the simulation state and context

    basically, this class is the "brain" that runs the world.
*/
public class Engine {

    // simulation state holds all entities, resources, map, crew, etc.
    private final SimulationState state;

    // context groups engine + state together so commands can access both.
    private final Context context;

    // keeps track of how many ticks have passed
    private int currentTick;

    /*
        constructor creates the engine and attaches it to the given state.
        we also give the player some starting credits so they can build things.
    */
    public Engine(SimulationState state) {
        this.state = state;
        this.context = new Context(this, state);
        this.currentTick = 0;

        // starting credits so player can immediately build entities
        state.addResource(ResourceType.CREDITS, 1000);
    }

    // getter for simulation state (used by commands and main loop)
    public SimulationState getState() {
        return state;
    }

    // getter for context object (used by commands)
    public Context getContext() {
        return context;
    }

    // getter for current simulation tick
    public int getCurrentTick() {
        return currentTick;
    }

    /*
        this method advances the simulation by exactly one tick.
        here is what happens:
        - increment time
        - ask every producer to produce resources
        - ask every converter to convert input into output
        - ask every consumer to consume resources
        - record a snapshot for graphing later

        this is the core time progression method of the whole system.
    */
    public String nextTick() {
        currentTick++;

        // tick all producers (solar farm, mine, hydroponic farm, etc.)
        for (Producer p : state.getProducers()) {
            p.tick(context);
        }

        // tick all converters (smelter, reactor)
        for (Converter c : state.getConverters()) {
            c.tick(context);
        }

        // tick all consumers (launchpad)
        for (Consumer consumer : state.getConsumers()) {
            consumer.tick(context);
        }

        // store a copy of current resource values for the graph command
        state.updateHistory();

        return "Tick " + currentTick + " completed.";
    }

    /*
        this method saves the entire simulation to a file.

        why two formats?
        - CurrentTick,<n> is required by the unit test
        - TICK=<n> is required by our load(...) method

        why two formats per resource?
        - NAME,<v> is required by the save test (csv style)
        - NAME=<v> is required by load(...) and assignment spec
    */
    public String save(String filename) {
        File file = new File(filename);
        File parent = file.getParentFile();

        // if no folder provided, save inside "data/"
        if (parent == null) {
            File dataDir = new File("data");
            if (!dataDir.exists()) {
                dataDir.mkdirs();
            }
            file = new File(dataDir, filename);
        } else {
            // create folder if it doesn’t exist
            if (!parent.exists()) {
                parent.mkdirs();
            }
        }

        try (PrintWriter out = new PrintWriter(new FileWriter(file))) {

            // tick written in both formats for full compatibility
            out.println("CurrentTick," + currentTick);
            out.println("TICK=" + currentTick);

            // save all resources
            out.println("RESOURCES");
            for (var entry : state.getInventory().entrySet()) {
                String name = entry.getKey().name();
                int value = entry.getValue();

                // csv format (used in engine save test)
                out.println(name + "," + value);

                // key=value format (used by load())
                out.println(name + "=" + value);
            }

            // entities would be saved here if required by the assignment

            return "Saved simulation to " + filename;

        } catch (IOException e) {
            return "Error saving simulation: " + e.getMessage();
        }
    }

    /*
        loads a simulation from a file.
        
        important:
        - we only read the "TICK=<value>" style for ticks
        - we only read "RESOURCE=value" style for resources
        - csv lines (name,value) are ignored because not required here

        load resets all resources to zero, then applies values from the file,
        and finally records a history snapshot so graphing remains consistent.
    */
    public String load(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;

            // reset all resources before loading
            for (ResourceType type : ResourceType.values()) {
                state.updateResource(type, 0);
            }

            // read the file line by line
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }

                // load tick value from "TICK=<n>"
                if (line.startsWith("TICK=")) {
                    String val = line.substring("TICK=".length());
                    try {
                        currentTick = Integer.parseInt(val);
                    } catch (NumberFormatException ignored) {
                        // ignore bad tick formats
                    }
                }
                // ignore RESOURCES marker
                else if (line.equals("RESOURCES")) {
                    // nothing to do
                }
                // load resource value from "NAME=value"
                else if (line.contains("=")) {
                    String[] parts = line.split("=");
                    if (parts.length == 2) {
                        try {
                            ResourceType type = ResourceType.valueOf(parts[0]);
                            int amount = Integer.parseInt(parts[1]);
                            state.updateResource(type, amount);
                        } catch (IllegalArgumentException ignored) {
                            // ignore unknown resource types or invalid numbers
                        }
                    }
                }
            }

            // after loading, add a new snapshot to history
            state.updateHistory();

            return "Loaded simulation from " + filename;

        } catch (IOException e) {
            return "Error loading simulation: " + e.getMessage();
        }
    }

    /*
        this method creates some default starting entities.
        it is only used for testing or for starting the game quickly.

        these base entities give the player a working colony:
        - solar farm, hydroponic farm, mine, gas extractor, water extractor, house
        - smelter, reactor
        - launchpad
    */
    public void initialiseDefaults() {

        // add main resource producers
        state.addProducer(new SolarFarm("SolarFarm-1"));
        state.addProducer(new HydroponicFarm("HydroponicFarm-1"));
        state.addProducer(new Mine("Mine-1"));
        state.addProducer(new GasExtractor("GasExtractor-1"));
        state.addProducer(new WaterExtractor("WaterExtractor-1"));
        state.addProducer(new House("House-1"));

        // add converters
        state.addConverter(new Smelter("Smelter-1"));
        state.addConverter(new Reactor("Reactor-1"));

        // add consumer
        state.addConsumer(new LaunchPad("LaunchPad-1"));
    }
}
