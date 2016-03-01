package com.excuseme.rocketleaguelivestats.repository.rocketleaguetracker;

import com.excuseme.rocketleaguelivestats.model.Statistics;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class RocketLeagueTrackerStatisticsRepositoryTest {
    @Test
    public void testParseElit() throws Exception {
        RocketLeagueTrackerStatisticsRepository rocketLeagueStatsStatisticsRepository = new RocketLeagueTrackerStatisticsRepository();
        Statistics statistics = rocketLeagueStatsStatisticsRepository.find("76561198070673430", "steam");
        assertNotNull(statistics);
    }

}