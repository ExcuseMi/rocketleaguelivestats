package com.excuseme.rocketleaguelivestats.model;

import java.util.Objects;

public class Rank implements Comparable<Rank>{
    private Tier tier;
    private Integer rating;

    public Rank(Tier tier, Integer rating) {
        this.tier = tier;
        this.rating = rating;
    }

    public Tier getTier() {
        return tier;
    }

    public Integer getRating() {
        return rating;
    }

    @Override
    public String toString() {
        return "Rank{" +
                "tier=" + tier +
                ", rating=" + rating +
                '}';
    }


    @Override
    public int compareTo(Rank o) {
        if(Objects.equals(getTier().getTier(), o.getTier().getTier())) {
            return Integer.compare(getTier().getTier(), o.getTier().getTier());
        }
        return Integer.compare(getRating() != null ? getRating() : 0, o.getRating() != null ? o.getRating() : 0);
    }
}
