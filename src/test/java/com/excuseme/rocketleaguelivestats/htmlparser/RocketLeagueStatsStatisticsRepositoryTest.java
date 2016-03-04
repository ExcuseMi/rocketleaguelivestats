package com.excuseme.rocketleaguelivestats.htmlparser;

import com.excuseme.rocketleaguelivestats.model.Statistics;
import com.excuseme.rocketleaguelivestats.repository.rocketleaguestats.RankParser;
import com.excuseme.rocketleaguelivestats.repository.rocketleaguestats.RocketLeagueStatsStatisticsRepository;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;
@Ignore
public class RocketLeagueStatsStatisticsRepositoryTest {
    @Test
    public void testParse() throws Exception {
        RocketLeagueStatsStatisticsRepository rocketLeagueStatsStatisticsRepository = new RocketLeagueStatsStatisticsRepository(new RankParser());
        Statistics statistics = rocketLeagueStatsStatisticsRepository.find("76561197996066422", "steam");
        assertNotNull(statistics);
//        assertEquals(Integer.valueOf(555), statistics.getOneVsOne());
//        assertEquals(Integer.valueOf(573), statistics.getTwoVsTwo());
//        assertEquals(Integer.valueOf(359), statistics.getThreeVSThreeSolo());
//        assertEquals(Integer.valueOf(437), statistics.getThreeVsThree());

    }

    @Test
    public void testParseElit() throws Exception {
        RocketLeagueStatsStatisticsRepository rocketLeagueStatsStatisticsRepository = new RocketLeagueStatsStatisticsRepository(new RankParser());
        Statistics statistics = rocketLeagueStatsStatisticsRepository.find("76561198070673430", "steam");
        assertNotNull(statistics);
//        assertEquals(Integer.valueOf(555), statistics.getOneVsOne());
//        assertEquals(Integer.valueOf(573), statistics.getTwoVsTwo());
//        assertEquals(Integer.valueOf(359), statistics.getThreeVSThreeSolo());
//        assertEquals(Integer.valueOf(437), statistics.getThreeVsThree());

    }

}