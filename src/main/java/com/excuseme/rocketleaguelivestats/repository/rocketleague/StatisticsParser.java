package com.excuseme.rocketleaguelivestats.repository.rocketleague;

import com.excuseme.rocketleaguelivestats.model.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StatisticsParser {
    private static final Pattern PLAYLIST_PATTERN = Pattern.compile("^Playlist=(\\d+).*");

    public Statistics parse(String text) throws UnsupportedEncodingException {
        final String[] lines = text.split("\\n|\\r");
        final Statistics statistics = new Statistics();
        statistics.setOneVsOne(new Rank(Tier.UNRANKED, 0, null, null));
        statistics.setTwoVsTwo(new Rank(Tier.UNRANKED, 0, null, null));
        statistics.setThreeVSThreeSolo(new Rank(Tier.UNRANKED, 0, null, null));
        statistics.setThreeVsThree(new Rank(Tier.UNRANKED, 0, null, null));

        for (String line : lines) {
            final Matcher matcher = PLAYLIST_PATTERN.matcher(line);
            if (matcher.matches()) {
                final PlaylistType playlistType = PlaylistType.findByPlaylistId(Integer.parseInt(matcher.group(1)));
                if (playlistType.isRanked()) {
                    Rank rank = parseLine(line);
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

    private Rank parseLine(String line) throws UnsupportedEncodingException {
        final String[] parameters = URLDecoder.decode(line, "UTF-8").split("&");
        BigDecimal mu = new BigDecimal(0);
        BigDecimal sigma = new BigDecimal(0);
        BigDecimal mmr = new BigDecimal(0);
        Tier tier = Tier.UNRANKED;
        Integer division = 0;
        Integer matchesPlayed = 0;
        Integer rating = 0;

        for (String parameter : parameters) {
            final String[] parameterParts = parameter.split("=");
            if (2 == parameterParts.length) {
                switch (parameterParts[0]) {
                    case "Mu":
                        mu = new BigDecimal(parameterParts[1]);
                        break;
                    case "Sigma":
                        sigma = new BigDecimal(parameterParts[1]);
                        break;
                    case "MMR":
                        mmr = new BigDecimal(parameterParts[1]);
                        break;
                    case "Tier":
                        tier = Tier.findByTier(Integer.parseInt(parameterParts[1]));
                        break;
                    case "Division":
                        division = Integer.parseInt(parameterParts[1]);
                        break;
                    case "MatchesPlayed":
                        matchesPlayed = Integer.parseInt(parameterParts[1]);
                        break;
                    case "Rating":
                        rating = Integer.parseInt(parameterParts[1]);
                        break;
                }
            }
        }
        final Skill skill = new Skill(mu, sigma, mmr, matchesPlayed);
        final Rank rank = new Rank(tier, division, rating, null);
        rank.setSkill(skill);
        return rank;
    }
}
