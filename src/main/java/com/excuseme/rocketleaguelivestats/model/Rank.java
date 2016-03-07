package com.excuseme.rocketleaguelivestats.model;

import java.math.BigDecimal;

public class Rank implements Comparable<Rank>{
    private Tier tier;
    private Integer division;
    private Skill skill;

    public Rank(Tier tier, Integer division) {
        this.tier = tier;
        this.division = division;
    }

    public Tier getTier() {
        return tier;
    }

    public Integer getDivision() {
        return division;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    @Override
    public String toString() {
        return "Rank{" +
                "tier=" + tier +
                ", division=" + division +
                ", skill=" + skill +
                '}';
    }

    @Override
    public int compareTo(Rank o) {
        final BigDecimal mmr1 = getSkill() != null ? getSkill().getMmr() : new BigDecimal("0");
        final BigDecimal mmr2 = o.getSkill() != null ? o.getSkill().getMmr() : new BigDecimal("0");
        return mmr1.compareTo(mmr2);
    }
}
