package com.excuseme.rocketleaguelivestats.repository;

import com.excuseme.rocketleaguelivestats.model.Statistics;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class StatisticsRepository {

    private static final String AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36";

    public Statistics find(String playerId, String system)  {
        Document doc;
        Statistics statistics = null;

        try {
            doc = Jsoup.connect(createURL( playerId , system))
                    .userAgent(AGENT)
                    .timeout(100*1000).get();
            Elements select = doc.select("div:contains(Ranked)");
            Element last = select.last();
            Elements select1 = last.parent().select("table").first().select("td");
            statistics = new Statistics();
            for(int i = 0; i< select1.size(); i+=2) {
                Element element = select1.get(i);
                String text = element.text();
                if("1v1".equalsIgnoreCase(text)) {
                    statistics.setOneVsOne(Integer.parseInt(select1.get(i+1).text()));
                }
                if("2v2".equalsIgnoreCase(text)) {
                    statistics.setTwoVsTwo(Integer.parseInt(select1.get(i+1).text()));
                }
                if("3v3 Solo".equalsIgnoreCase(text)) {
                    statistics.setThreeVSThreeSolo(Integer.parseInt(select1.get(i+1).text()));
                }
                if("3v3".equalsIgnoreCase(text)) {
                    statistics.setThreeVsThree(Integer.parseInt(select1.get(i+1).text()));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return statistics;
    }

    public static String createURL(String playerId, String system) {
        return "https://rocketleaguestats.com/profile/" + playerId + "/" + system;
    }
}
