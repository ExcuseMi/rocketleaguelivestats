package com.excuseme.rocketleaguelivestats.repository.rocketleaguestats;

import com.excuseme.rocketleaguelivestats.model.Rank;
import com.excuseme.rocketleaguelivestats.model.Tier;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RankParser {
    private static final Pattern LEADERBOARD_POSITION = Pattern.compile("^#(\\d+).*");
    private static final Pattern TIER = Pattern.compile(".*Tier (\\d+).*");
    private static final Pattern RATING = Pattern.compile(".*Rating (\\d+).*");
    private static final Pattern DIVISION = Pattern.compile(".*Division (\\d+).*");

    public Rank parseRanked(String text) {
        final Integer leaderboardPosition = getNumber(text, LEADERBOARD_POSITION);
        final Integer tier = getNumber(text, TIER);
        final Integer rating = getNumber(text, RATING);
        final Integer division = getNumber(text, DIVISION);

        final Tier byTier = Tier.findByTier(tier);
        if(byTier  != null) {
            return new Rank(byTier, division, rating, leaderboardPosition);
        }
        return null;
    }

    private Integer getNumber(String text, Pattern pattern) {
        final Matcher matcher = pattern.matcher(text);
        if (matcher.matches()) {
            final String group = matcher.group(1);
            return Integer.parseInt(group);
        }
        return null;
    }
}
