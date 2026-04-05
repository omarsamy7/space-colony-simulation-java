package org.uob.a2.parser;

import org.uob.a2.engine.Context;

import java.util.List;

/*
    this command assigns population (crew) to different roles in the colony.

    available roles:
    - farm crew   → boosts food production
    - mine crew   → boosts ore production
    - reactor crew → boosts fuel production

    purpose of the assign command:
    - gives the player control over how population is distributed
    - allows strategic decisions (where should crew work?)
    - makes production dynamic instead of fixed

    this command checks:
    - correct input format
    - valid numbers
    - enough free population
    - valid roles
*/
public class AssignCommand extends Command {

    /*
        normal constructor that accepts parsed user words.
    */
    public AssignCommand(List<String> words) {
        super(words);
    }

    /*
        empty constructor for flexibility.
    */
    public AssignCommand() {
        super();
    }

    /*
        main execution of the assign command.
    */
    @Override
    public String execute(Context ctx) {

        // must include role and amount
        if (words == null || words.size() < 3) {
            return "Usage: assign <farm|mine|reactor> <amount>";
        }

        String role = words.get(1).toLowerCase();
        String amountStr = words.get(2);

        // try to turn the amount into a number
        int amount;
        try {
            amount = Integer.parseInt(amountStr);
        } catch (NumberFormatException e) {
            return "Amount must be a number.";
        }

        // amount must be positive
        if (amount <= 0) {
            return "Amount must be positive.";
        }

        // check if we have enough free population
        int free = ctx.state().getFreePopulation();
        if (free < amount) {
            return "Not enough free population. Free: " + free;
        }

        /*
            assign crew based on role
            we simply increase the crew count for that role
        */
        switch (role) {
            case "farm" -> 
                ctx.state().setFarmCrew(ctx.state().getFarmCrew() + amount);

            case "mine" -> 
                ctx.state().setMineCrew(ctx.state().getMineCrew() + amount);

            case "reactor" -> 
                ctx.state().setReactorCrew(ctx.state().getReactorCrew() + amount);

            default -> {
                // unknown role typed by the user
                return "Unknown role '" + role + "'. Use farm, mine, or reactor.";
            }
        }

        // success message returned to the player
        return "Assigned " + amount + " population to " + role +
                ". Free remaining: " + ctx.state().getFreePopulation();
    }
}
