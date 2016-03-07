package com.excuseme.rocketleaguelivestats.model;

import java.math.BigDecimal;

public class Skill {
    private BigDecimal mu;
    private BigDecimal sigma;
    private BigDecimal mmr;
    private Integer matchesPlayed;

    public Skill(BigDecimal mu, BigDecimal sigma, BigDecimal mmr, Integer matchesPlayed) {
        this.mu = mu;
        this.sigma = sigma;
        this.mmr = mmr;
        this.matchesPlayed = matchesPlayed;
    }

    public BigDecimal getMu() {
        return mu;
    }

    public BigDecimal getSigma() {
        return sigma;
    }

    public BigDecimal getMmr() {
        return mmr;
    }

    public Integer getMatchesPlayed() {
        return matchesPlayed;
    }

    @Override
    public String toString() {
        return "mmr=" + mmr;
    }


}
