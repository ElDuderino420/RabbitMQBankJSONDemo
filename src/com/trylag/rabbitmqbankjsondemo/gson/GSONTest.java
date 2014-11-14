package com.trylag.rabbitmqbankjsondemo.gson;

import com.google.gson.Gson;
import com.trylag.rabbitmqbankjsondemo.entity.LoanRequest;

/**
 *
 * @author Richard Haley III
 */
public class GSONTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if (args.length != 4) {
            System.out.println(" [X] Format for loan request is: ssn, creditScore, loanAmount, loanDuration ");
            exit();
        }

        try {
            LoanRequest loanRequest = new LoanRequest(Long.parseLong(args[0]), Integer.parseInt(args[1]), Double.parseDouble(args[2]), Integer.parseInt(args[3]));
            
            Gson gson = new Gson();
            String loanRequestJSON = gson.toJson(loanRequest);
            
            System.out.println(" [X] GSON returns the following JSON ");
            System.out.println(loanRequestJSON);
            
        } catch (NumberFormatException ex) {
            System.out.println(" [X] Caught NumberFormatException while parsing main method arguments ");
            exit();
        }
    }

    private static void exit() {
        System.out.println("... exiting ");
        System.exit(1);
    }
}
