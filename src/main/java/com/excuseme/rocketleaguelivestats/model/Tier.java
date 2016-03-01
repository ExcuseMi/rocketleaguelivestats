package com.excuseme.rocketleaguelivestats.model;

public enum Tier {
    UNRANKED(0, "Unranked"),
    PROSPECT_1(1, "Prospect I"),
    PROSPECT_2(2, "Prospect II"),
    PROSPECT_3(3, "Prospect III"),
    PROSPECT_ELITE(4, "Prospect Elite"),
    CHALLENGER_1(5, "Challenger I"),
    CHALLENGER_2(6, "Challenger II"),
    CHALLENGER_3(7, "Challenger III"),
    CHALLENGER_ELITE(8, "Challenger Elite"),
    RISING_STAR(9, "Rising Star"),
    SHOOTING_STAR(10, "Shooting Star"),
    ALL_STAR(11, "All-Star"),
    SUPERSTAR(12, "Superstar"),
    CHAMPION(13, "Champion"),
    SUPER_CHAMPION(14, "Super Champion"),
    GRAND_CHAMPION(15, "Grand Champion");

    private int tier;
    private String text;

    Tier(int tier, String text) {
        this.tier = tier;
        this.text = text;
    }

    public int getTier() {
        return tier;
    }

    public String getText() {
        return text;
    }

    public static Tier findByTier(Integer tier) {
        for(Tier rank: values()) {
            if(rank.getTier() == tier) {
                return rank;
            }
        }
        return null;
    }

    public static Tier findByName(String tier) {
        for(Tier rank: values()) {
            if(rank.getText().equalsIgnoreCase(tier)) {
                return rank;
            }
        }
        return null;

    }
}
