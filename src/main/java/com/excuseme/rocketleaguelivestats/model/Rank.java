package com.excuseme.rocketleaguelivestats.model;

public class Rank implements Comparable<Rank>{
    private Tier tier;
    private Integer division;
    private Integer rating;

    public Rank(Tier tier, Integer division, Integer rating) {
        this.tier = tier;
        this.division = division;
        this.rating = rating;
    }

    public Tier getTier() {
        return tier;
    }

    public Integer getDivision() {
        return division;
    }

    public Integer getRating() {
        return rating;
    }

    @Override
    public int compareTo(Rank o) {
        final Integer rating1 = getRating() != null ? getRating() : 0;
        final Integer rating2 = o.getRating() != null ? getRating() :0;
        return rating1.compareTo(rating2);
    }
}
