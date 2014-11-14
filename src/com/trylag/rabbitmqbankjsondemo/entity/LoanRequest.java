package com.trylag.rabbitmqbankjsondemo.entity;

/**
 *
 * @author Richard Haley III
 */
public class LoanRequest {

    private long ssn;
    private int creditScore;
    private double loanAmount;
    private int loanDuration;

    public LoanRequest(long ssn, int creditScore, double loanAmount, int loanDuration) {
        this.ssn = ssn;
        this.creditScore = creditScore;
        this.loanAmount = loanAmount;
        this.loanDuration = loanDuration;
    }
}
