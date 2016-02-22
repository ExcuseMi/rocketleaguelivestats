package com.excuseme.rocketleaguelivestats.repository;

import com.excuseme.rocketleaguelivestats.model.Rank;
import com.excuseme.rocketleaguelivestats.model.Tier;
import com.excuseme.rocketleaguelivestats.model.Statistics;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class StatisticsRepository {

    private static final String AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36";
    private RankParser rankParser;
    private final static Logger LOGGER = Logger.getLogger(StatisticsRepository.class);
    public StatisticsRepository(RankParser rankParser) {
        this.rankParser = rankParser;
    }

    public Statistics find(String playerId, String system)  {
        Document doc;
        Statistics statistics = null;

        try {
            final String url = createURL(playerId, system);
            doc = Jsoup.connect(url)
                    .userAgent(AGENT)
                    .timeout(100*1000).get();
            LOGGER.info("Querying " + url);
            final Element season2 = doc.select("div#season_2").last();
            final Rank duel = rankParser.parseRanked(findRank(season2, "Solo Duel"));
            final Rank doubles = rankParser.parseRanked(findRank(season2, "Doubles"));
            final Rank soloStandard = rankParser.parseRanked(findRank(season2, "Solo Standard"));
            final Rank standard = rankParser.parseRanked(findRank(season2, "Standard"));

            statistics = new Statistics();
            statistics.setOneVsOne(duel);
            statistics.setTwoVsTwo(doubles);
            statistics.setThreeVSThreeSolo(soloStandard);
            statistics.setThreeVsThree(standard);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return statistics;
    }

    private String findRank(Element season2, String division) {
        Elements select = season2.select("div:contains("+ division +")");
        Element last = select.last();
        return last.parent().select("p").first().firstElementSibling().text();
    }

    public static String createURL(String playerId, String system) {
        return "https://rocketleaguestats.com/profile/" + playerId + "/" + system;
    }
}
