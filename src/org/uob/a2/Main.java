package org.uob.a2;

import org.uob.a2.engine.Context;
import org.uob.a2.engine.Engine;
import org.uob.a2.engine.SimulationState;
import org.uob.a2.parser.Command;
import org.uob.a2.parser.Parser;
import org.uob.a2.parser.QuitCommand;

import java.util.Scanner;

/*
    this is the entry point for the whole simulation
    the main goal here is to:
    - create the simulation state
    - create the engine that controls everything
    - read user input
    - turn that input into commands using the parser
    - execute these commands and print results
    - keep looping until the user quits

    basically, this file connects the player (input)
    to the simulation logic (engine + commands).
*/
public class Main {

    public static void main(String[] args) {

        /*
            create a fresh simulation state.
            this object holds all the resources, the map,
            all entities (producers, converters, consumers),
            crew assignments, and so on.
        */
        SimulationState state = new SimulationState();

        /*
            create the engine which controls time (ticks),
            saving/loading, and gives us access to the state.
        */
        Engine engine = new Engine(state);

        /*
            load some default starting entities
            (solar farm, mine, launchpad, etc.)
            this gives the simulation something to start with.
        */
        engine.initialiseDefaults();

        /*
            the parser is responsible for turning the user's
            text input into the right command object.
        */
        Parser parser = new Parser();

        /*
            context carries a reference to both the engine
            and the state, so commands can interact with them.
        */
        Context ctx = engine.getContext();

        /*
            scanner reads user input from the terminal.
        */
        Scanner scanner = new Scanner(System.in);

        // this keeps the main loop running until user types quit.
        boolean running = true;

        // simple welcome message.
        System.out.println("=== Space Colony Simulator ===");
        System.out.println("Type 'help' or 'info' to see your current status.");

        /*
            main loop:
            keep reading commands from the user,
            parse them, execute them, print the result.
        */
        while (running) {
            System.out.print("> ");

            // if no more input, stop the loop.
            if (!scanner.hasNextLine()) {
                break;
            }

            // read the full line the user typed.
            String line = scanner.nextLine();

            // parse it into a specific command object.
            Command cmd = parser.parse(line);

            // execute the command and get the output message.
            String result = cmd.execute(ctx);

            // show the result to the user.
            System.out.println(result);

            // if user typed quit, stop the loop.
            if (cmd instanceof QuitCommand) {
                running = false;
            }
        }

        // final message after exiting simulation.
        System.out.println("Simulation ended.");
    }
}
