package com.excuseme.rocketleaguelivestats.repository.rocketleague;

import com.excuseme.rocketleaguelivestats.model.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatisticsParser {
    private static final Pattern PLAYLIST_PATTERN = Pattern.compile("^Playlist=(\\d+)&Mu=(\\d*\\.?\\d*)&Sigma=(\\d*\\.?\\d*)&Tier=(\\d+)&Division=(\\d+)&MatchesPlayed=(\\d+)&MMR=(\\d*\\.?\\d*)$");

    public Statistics parse(String text) throws UnsupportedEncodingException {
        if(text == null || "".equals(text)) {
            return null;
        }
        final String[] lines = text.split("\\n|\\r");
        final Statistics statistics = new Statistics();

        statistics.setOneVsOne(new Rank(Tier.UNRANKED, null));
        statistics.setTwoVsTwo(new Rank(Tier.UNRANKED, null));
        statistics.setThreeVSThreeSolo(new Rank(Tier.UNRANKED, null));
        statistics.setThreeVsThree(new Rank(Tier.UNRANKED, null));

        for (String line : lines) {
            final Matcher matcher = PLAYLIST_PATTERN.matcher(line);
            if (matcher.matches()) {
                final PlaylistType playlistType = PlaylistType.findByPlaylistId(Integer.parseInt(matcher.group(1)));
                if (playlistType.isRanked()) {
                    Rank rank = parseLine(matcher);
                    switch (playlistType) {
                        case DUEL_RANKED:
                            statistics.setOneVsOne(rank);
                            break;
                        case DOUBLES_RANKED:
                            statistics.setTwoVsTwo(rank);
                            break;
                        case SOLO_STANDARD_RANKED:
                            statistics.setThreeVSThreeSolo(rank);
                            break;
                        case STANDARD_RANKED:
                            statistics.setThreeVsThree(rank);
                            break;
                    }
                }

            }
        }

        return statistics;
    }

    private Rank parseLine(Matcher matcher) throws UnsupportedEncodingException {
        BigDecimal mu = new BigDecimal(matcher.group(2));
        BigDecimal sigma = new BigDecimal(matcher.group(3));
        BigDecimal mmr = new BigDecimal(matcher.group(7));
        Tier tier = Tier.findByTier( Integer.parseInt(matcher.group(4)));
        Integer division = Integer.parseInt(matcher.group(5))+1;
        Integer matchesPlayed = Integer.parseInt(matcher.group(6));

        final Skill skill = new Skill(mu, sigma, mmr, matchesPlayed);
        final Rank rank = new Rank(tier, division);
        rank.setSkill(skill);
        return rank;
    }
}
