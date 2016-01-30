package com.excuseme.rocketleaguelivestats.htmlparser;

import com.excuseme.rocketleaguelivestats.model.Statistics;
import com.excuseme.rocketleaguelivestats.repository.StatisticsRepository;
import org.junit.Test;

import static org.junit.Assert.*;

public class StatisticsRepositoryTest {
    @Test
    public void testParse() throws Exception {
        StatisticsRepository statisticsRepository = new StatisticsRepository();
        Statistics statistics = statisticsRepository.find("kronovi", "steam");
        assertNotNull(statistics);
//        assertEquals(Integer.valueOf(555), statistics.getOneVsOne());
//        assertEquals(Integer.valueOf(573), statistics.getTwoVsTwo());
//        assertEquals(Integer.valueOf(359), statistics.getThreeVSThreeSolo());
//        assertEquals(Integer.valueOf(437), statistics.getThreeVsThree());

    }
}