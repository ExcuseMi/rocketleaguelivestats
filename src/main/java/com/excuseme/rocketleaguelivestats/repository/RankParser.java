package com.excuseme.rocketleaguelivestats.repository;

import com.excuseme.rocketleaguelivestats.model.Rank;
import com.excuseme.rocketleaguelivestats.model.Tier;

public class RankParser {
    public static final String TIER = "(Tier ";
    public static final String RATING = "- Rating ";

    public Rank parseRanked(String text) {
        if(text.contains(TIER)) {
            String tierText;
            String rating = null;
            if(!text.substring(text.indexOf(TIER) + TIER.length()).contains(RATING)) {
                tierText = text.substring(text.indexOf(TIER) + TIER.length(), text.indexOf(")")).trim();
            } else {
                tierText = text.substring(text.indexOf(TIER) + TIER.length(), text.indexOf("-")).trim();
                rating = text.substring(text.indexOf(RATING) + RATING.length(), text.indexOf(")")).trim();
            }
            final Tier tier = Tier.findByTier(Integer.parseInt(tierText));
            return new Rank(tier, rating != null ? Integer.parseInt(rating) : null);

        }
        return null;
    }
}
