package org.uob.a2.engine;

import org.uob.a2.model.ResourceType;

import java.util.List;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Map;
import java.util.Random;

/*
    this class stores everything about the simulation world.
    it acts like a big container that holds:

    - all entities (producers, converters, consumers)
    - all resource values (credits, ore, metal, etc.)
    - the history of resources for graphing
    - the map state (player location, explored tiles, locked areas)
    - crew assignments (farm crew, mine crew, reactor crew)

    basically, the simulation state = the entire world at any moment in time.
    the engine interacts with this class every tick.
*/
public class SimulationState {

    // lists storing all entities currently active in the simulation
    private List<Producer> producers;
    private List<Converter> converters;
    private List<Consumer> consumers;

    // main inventory storing all resources and their amounts
    private Map<ResourceType, Integer> inventory = new EnumMap<>(ResourceType.class);

    // history of resource snapshots (used for the graph command)
    private List<Map<ResourceType, Integer>> resourceHistory = new ArrayList<>();

    // ----------------------------------------------------------
    // crew and population system
    // ----------------------------------------------------------

    // number of crew assigned to each role
    private int farmCrew;
    private int mineCrew;
    private int reactorCrew;

    // ----------------------------------------------------------
    // map and exploration system
    // ----------------------------------------------------------

    private static final int MAP_SIZE = 10;  // size of the square map
    private final char[][] map;              // what symbols appear on each tile
    private final boolean[][] explored;      // which tiles were explored
    private final int centreX;
    private final int centreY;
    private int playerX;
    private int playerY;

    // how far out from the center the player is allowed to move/explore
    private int unlockedRadius;

    // used for random exploration events
    private final Random random = new Random();

    /*
        constructor sets up everything:
        - creates empty lists for entities
        - initializes all resources to 0
        - resets crew
        - builds a fresh unexplored map
        - places the player at the map center
        - unlocks a small starting area
    */
    public SimulationState() {
        // entity lists start empty and engine adds default ones
        producers = new ArrayList<>();
        converters = new ArrayList<>();
        consumers = new ArrayList<>();

        // set all resources to 0 to avoid null checks later
        for (ResourceType type : ResourceType.values()) {
            inventory.put(type, 0);
        }

        // crew roles start empty (user assigns later)
        farmCrew = 0;
        mineCrew = 0;
        reactorCrew = 0;

        // create a fresh unexplored 10x10 map
        map = new char[MAP_SIZE][MAP_SIZE];
        explored = new boolean[MAP_SIZE][MAP_SIZE];

        for (int y = 0; y < MAP_SIZE; y++) {
            for (int x = 0; x < MAP_SIZE; x++) {
                map[y][x] = '?';          // unknown tile
                explored[y][x] = false;   // not explored yet
            }
        }

        // place player at center
        centreX = MAP_SIZE / 2;
        centreY = MAP_SIZE / 2;
        playerX = centreX;
        playerY = centreY;

        // small initial unlocked area around center
        unlockedRadius = 2;

        // mark starting tile as explored
        explored[playerY][playerX] = true;
        map[playerY][playerX] = 'P';
    }

    // ----------------------------------------------------------
    // entity list access
    // ----------------------------------------------------------

    // getters for all entity lists
    public List<Producer> getProducers() { return producers; }
    public List<Converter> getConverters() { return converters; }
    public List<Consumer> getConsumers()  { return consumers; }

    // add new entities to the world
    public void addProducer(Producer producer) { producers.add(producer); }
    public void addConverter(Converter converter) { converters.add(converter); }
    public void addConsumer(Consumer consumer) { consumers.add(consumer); }

    // ----------------------------------------------------------
    // resource management
    // ----------------------------------------------------------

    /*
        adds an amount to a resource.
        why we check for 0:
        - avoids unnecessary map updates
    */
    public void addResource(ResourceType resource, int amount) {
        if (amount == 0) return;
        int current = inventory.getOrDefault(resource, 0);
        inventory.put(resource, current + amount);
    }

    /*
        sets a resource to an exact value (used for loading).
    */
    public void updateResource(ResourceType resource, int amount) {
        inventory.put(resource, amount);
    }

    /*
        get the current amount of a resource.
    */
    public int getResourceAmount(ResourceType resource) {
        return inventory.getOrDefault(resource, 0);
    }

    /*
        removes resources if possible.
        returns true if removal succeeded, false if not enough resources.
    */
    public boolean removeResource(ResourceType resource, int amount) {
        if (amount < 0) return false;
        int current = inventory.getOrDefault(resource, 0);
        if (current < amount) return false;
        inventory.put(resource, current - amount);
        return true;
    }

    /*
        stores a snapshot of the inventory for graphing.
        this runs every tick.
    */
    public void updateHistory() {
        Map<ResourceType, Integer> snapshot = new EnumMap<>(ResourceType.class);
        for (ResourceType type : ResourceType.values()) {
            snapshot.put(type, inventory.getOrDefault(type, 0));
        }
        resourceHistory.add(snapshot);
    }

    // getters for inventory and history
    public Map<ResourceType, Integer> getInventory() { return inventory; }
    public List<Map<ResourceType, Integer>> getResourceHistory() { return resourceHistory; }
    public List<Map<ResourceType, Integer>> getHistory() { return resourceHistory; }

    // ----------------------------------------------------------
    // crew / population helpers
    // ----------------------------------------------------------

    public int getFarmCrew() { return farmCrew; }
    public int getMineCrew() { return mineCrew; }
    public int getReactorCrew() { return reactorCrew; }

    // safely update crew assignments
    public void setFarmCrew(int farmCrew) { this.farmCrew = Math.max(0, farmCrew); }
    public void setMineCrew(int mineCrew) { this.mineCrew = Math.max(0, mineCrew); }
    public void setReactorCrew(int reactorCrew) { this.reactorCrew = Math.max(0, reactorCrew); }

    // total assigned population
    public int getAssignedPopulation() {
        return farmCrew + mineCrew + reactorCrew;
    }

    /*
        free population = total population - assigned.
        always clamp to at least 0.
    */
    public int getFreePopulation() {
        int total = getResourceAmount(ResourceType.POPULATION);
        int free = total - getAssignedPopulation();
        return Math.max(0, free);
    }

    /*
        ensures crew assignments never exceed total population
        (this can happen after a launch reduces population).
    */
    public void clampCrewToPopulation() {
        int total = getResourceAmount(ResourceType.POPULATION);
        int assigned = getAssignedPopulation();

        if (assigned <= total) return;

        // scale down proportionally
        double factor = (total <= 0) ? 0.0 : (double) total / assigned;
        farmCrew = (int) Math.floor(farmCrew * factor);
        mineCrew = (int) Math.floor(mineCrew * factor);
        reactorCrew = (int) Math.floor(reactorCrew * factor);
    }

    // ----------------------------------------------------------
    // map system helpers
    // ----------------------------------------------------------

    // check if coordinates are within map bounds
    private boolean isInsideMap(int x, int y) {
        return x >= 0 && x < MAP_SIZE && y >= 0 && y < MAP_SIZE;
    }

    // check if the tile is within the unlocked movement radius
    private boolean isUnlocked(int x, int y) {
        int dx = Math.abs(x - centreX);
        int dy = Math.abs(y - centreY);
        return Math.max(dx, dy) <= unlockedRadius;
    }

    /*
        builds a text version of the map:
        - '#' locked tile
        - 'P' player tile
        - '.' explored tile
        - '?' unexplored but unlocked
    */
    public String renderMap() {
        StringBuilder sb = new StringBuilder("Map:\n");

        for (int y = 0; y < MAP_SIZE; y++) {
            for (int x = 0; x < MAP_SIZE; x++) {
                char symbol;

                if (!isUnlocked(x, y)) symbol = '#';
                else if (x == playerX && y == playerY) symbol = 'P';
                else if (explored[y][x]) symbol = '.';
                else symbol = '?';

                sb.append('[').append(symbol).append(']');
            }
            sb.append('\n');
        }

        return sb.toString().trim();
    }

    /*
        moves the player on the map if the destination tile is valid.
        returns true if movement succeeded, false otherwise.
    */
    public boolean movePlayer(String direction) {
        int dx = 0, dy = 0;

        switch (direction.toLowerCase()) {
            case "north" -> dy = -1;
            case "south" -> dy = 1;
            case "west"  -> dx = -1;
            case "east"  -> dx = 1;
            default -> { return false; }
        }

        int newX = playerX + dx;
        int newY = playerY + dy;

        if (!isInsideMap(newX, newY)) return false;
        if (!isUnlocked(newX, newY)) return false;

        playerX = newX;
        playerY = newY;

        return true;
    }

    /*
        explores the current tile and triggers a random event.
        only works if the tile is unlocked and not explored yet.
    */
    public String exploreCurrentTile() {
        if (!isUnlocked(playerX, playerY)) {
            return "This area is not unlocked yet.";
        }

        if (explored[playerY][playerX]) {
            return "You have already explored this area.";
        }

        explored[playerY][playerX] = true;

        // generate random bonus or penalty
        int roll = random.nextInt(5);
        String event;

        switch (roll) {
            case 0 -> {
                addResource(ResourceType.WATER, 10);
                event = "You discovered an ice deposit: +10 WATER.";
            }
            case 1 -> {
                addResource(ResourceType.GAS, 10);
                event = "You found a rich gas field: +10 GAS.";
            }
            case 2 -> {
                addResource(ResourceType.ORE, 10);
                event = "You found an ore vein: +10 ORE.";
            }
            case 3 -> {
                addResource(ResourceType.METAL, 20);
                event = "You recovered metal from a crashed probe: +20 METAL.";
            }
            default -> {
                boolean lost = removeResource(ResourceType.ENERGY, 5);
                if (lost) {
                    event = "A dust storm hit the colony: -5 ENERGY.";
                } else {
                    event = "A dust storm hit, but you had no ENERGY to lose.";
                }
            }
        }

        return "Area explored.\n" + event;
    }

    /*
        increases the unlocked radius when the launchpad levels up.
        higher level = bigger explored zone.
    */
    public void updateUnlockedRadiusForLevel(int launchLevel) {
        if (launchLevel <= 1) unlockedRadius = 2;
        else if (launchLevel == 2) unlockedRadius = 3;
        else unlockedRadius = 4;  // most of the map
    }
}
