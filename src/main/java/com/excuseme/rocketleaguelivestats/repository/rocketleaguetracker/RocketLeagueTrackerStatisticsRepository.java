package com.excuseme.rocketleaguelivestats.repository.rocketleaguetracker;

import com.excuseme.rocketleaguelivestats.model.Rank;
import com.excuseme.rocketleaguelivestats.model.Statistics;
import com.excuseme.rocketleaguelivestats.model.Tier;
import com.excuseme.rocketleaguelivestats.repository.StatisticsRepository;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RocketLeagueTrackerStatisticsRepository implements StatisticsRepository {
    private static final String AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36";
    private final static Logger LOGGER = Logger.getLogger(RocketLeagueTrackerStatisticsRepository.class);
    public static final Pattern DIVISION_PATTERN = Pattern.compile("Division ([IVX]+)");

    @Override
    public Statistics find(String playerId, String system) {
        Document doc;
        Statistics statistics = null;

        try {
            final String url = createUrl(playerId, system);
            doc = Jsoup.connect(url)
                    .userAgent(AGENT)
                    .timeout(100*1000).get();
            LOGGER.info("Querying " + url);
            final ListIterator<Element> elementListIterator = doc.select("div.stats-tile-50").listIterator();
            final List<Rank> rankList = new ArrayList<>();
            while(elementListIterator.hasNext()){
                final Element next = elementListIterator.next();
                rankList.add(parse(next));
            }
            statistics = new Statistics();
            statistics.setOneVsOne(rankList.get(0));
            statistics.setTwoVsTwo(rankList.get(1));
            statistics.setThreeVSThreeSolo(rankList.get(2));
            statistics.setThreeVsThree(rankList.get(3));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return statistics;

    }

    @Override
    public String createUrl(String playerId, String system) {
        return "http://rocketleague.tracker.network/profile/" + system + "/" + playerId;
    }

    private Rank parse(Element element) {
        //Division V
        final String divisionText = element.getAllElements().get(3).text();
        final Integer division = parseDivision(divisionText);
        //Grand Champion
        final String tierName = element.getAllElements().get(4).text();
        Tier tier = Tier.findByName(tierName);

        return new Rank(tier, division, null, null);
    }

    private Integer parseDivision(String divisionText) {
        final Matcher matcher = DIVISION_PATTERN.matcher(divisionText);
        if(matcher.matches()) {
            return toArabic(matcher.group(1));
        }
        return null;
    }

    private static int toArabic(String number) {
        if (Objects.equals(number, "")) return 0;
        if (number.startsWith("M")) return 1000 + toArabic(number.substring(1));
        if (number.startsWith("CM")) return 900 + toArabic(number.substring(2));
        if (number.startsWith("D")) return 500 + toArabic(number.substring(1));
        if (number.startsWith("CD")) return 400 + toArabic(number.substring(2));
        if (number.startsWith("C")) return 100 + toArabic(number.substring(1));
        if (number.startsWith("XC")) return 90 + toArabic(number.substring(2));
        if (number.startsWith("L")) return 50 + toArabic(number.substring(1));
        if (number.startsWith("XL")) return 40 + toArabic(number.substring(2));
        if (number.startsWith("X")) return 10 + toArabic(number.substring(1));
        if (number.startsWith("IX")) return 9 + toArabic(number.substring(2));
        if (number.startsWith("V")) return 5 + toArabic(number.substring(1));
        if (number.startsWith("IV")) return 4 + toArabic(number.substring(2));
        if (number.startsWith("I")) return 1 + toArabic(number.substring(1));
        throw new IllegalArgumentException("something bad happened");
    }
}
