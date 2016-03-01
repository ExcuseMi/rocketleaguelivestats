package com.excuseme.rocketleaguelivestats.model;

import java.util.Objects;

public class Rank implements Comparable<Rank>{
    private Tier tier;
    private Integer division;
    private Integer rating;
    private Integer leaderboardPosition;
    private Skill skill;

    public Rank(Tier tier, Integer division, Integer rating, Integer leaderboardPosition) {
        this.tier = tier;
        this.division = division;
        this.rating = rating;
        this.leaderboardPosition = leaderboardPosition;
    }

    public Tier getTier() {
        return tier;
    }

    public Integer getRating() {
        return rating;
    }

    public Integer getDivision() {
        return division;
    }

    public Integer getLeaderboardPosition() {
        return leaderboardPosition;
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
                ", rating=" + rating +
                ", leaderboardPosition=" + leaderboardPosition +
                '}';
    }

    @Override
    public int compareTo(Rank o) {
        if(Objects.equals(getTier().getTier(), o.getTier().getTier())) {
            if(Objects.equals(getDivision(), o.getDivision())) {
                if(Objects.equals(getRating(), o.getRating())) {
                    return -1 * Integer.compare(getLeaderboardPosition() != null ? getLeaderboardPosition() : Integer.MAX_VALUE, o.getLeaderboardPosition() != null ? o.getLeaderboardPosition() : Integer.MAX_VALUE);
                }
                return Integer.compare(getRating() != null ? getRating() : 0, o.getRating() != null ? o.getRating() : 0);
            }
            return Integer.compare(getDivision(), o.getDivision());
        }
        return Integer.compare(getTier().getTier(), o.getTier().getTier());

    }
}
