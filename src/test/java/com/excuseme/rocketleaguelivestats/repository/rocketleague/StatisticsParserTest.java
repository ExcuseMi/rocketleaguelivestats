package com.excuseme.rocketleaguelivestats.repository.rocketleague;

import com.excuseme.rocketleaguelivestats.model.Statistics;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

import static org.junit.Assert.*;

public class StatisticsParserTest {
    private StatisticsParser statisticsParser = new StatisticsParser();

    @Test
    public void test() throws UnsupportedEncodingException {
        final Statistics statistics = statisticsParser.parse("Playlist=0&Mu=47.8593&Sigma=2.5&Tier=&Division=&MatchesPlayed=&MMR=\r\n" +
                "Playlist=10&Mu=36.8985&Sigma=2.5&Tier=7&Division=2&MatchesPlayed=31&MMR=29.3985\r\n" +
                "Playlist=11&Mu=46.8857&Sigma=2.5&Tier=9&Division=2&MatchesPlayed=245&MMR=39.3857\n" +
                "Playlist=12&Mu=47.3424&Sigma=2.5&Tier=9&Division=3&MatchesPlayed=62&MMR=39.8424\n" +
                "Playlist=13&Mu=52.7735&Sigma=2.5&Tier=10&Division=3&MatchesPlayed=90&MMR=45.2735");
        assertNotNull(statistics);
        assertNotNull(statistics.getOneVsOne());
    }

}