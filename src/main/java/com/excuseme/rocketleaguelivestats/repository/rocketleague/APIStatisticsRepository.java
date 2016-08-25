package com.excuseme.rocketleaguelivestats.repository.rocketleague;


import com.excuseme.rocketleaguelivestats.model.*;
import com.excuseme.rocketleaguelivestats.repository.StatisticsRepository;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class APIStatisticsRepository implements StatisticsRepository {
    private final static String URL = "https://20kiyaost7.execute-api.us-west-2.amazonaws.com/prod";
    private static final String API_KEY = "wn6Xw8MfNP2hYvt8VOMla9q4YH6OAIGavvKhfRYj";

    @Override
    public Statistics find(String playerId, GamingSystem gamingSystem) {
        if(!GamingSystem.BOT.equals(gamingSystem)) {
            ClientConfig clientConfig = new DefaultClientConfig();
            clientConfig.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, Boolean.TRUE);
            Client client = Client.create(clientConfig);
            WebResource webResource = client.resource(URL);
            Payload payLoad = webResource.queryParam("platform",gamingSystem.getApiId().toString())
                    .queryParam("name", playerId)
                    .header("X-API-KEY", API_KEY)
                    .accept("application/json")
                    .get(Payload.class);
            if(payLoad != null) {
                Statistics statistics = new Statistics();
                Stat stat = findStat(payLoad.stats, "Ranked Duel 1v1");
                statistics.setOneVsOne(createRank(stat));
                stat = findStat(payLoad.stats, "Ranked Doubles 2v2");
                statistics.setTwoVsTwo(createRank(stat));
                stat = findStat(payLoad.stats, "Ranked Solo Standard 3v3");
                statistics.setThreeVSThreeSolo(createRank(stat));
                stat = findStat(payLoad.stats, "Ranked Standard 3v3");
                statistics.setThreeVsThree(createRank(stat));
                return statistics;
            }
        }
        return null;
    }

    private Rank createRank(Stat stat) {
        if(stat == null) {
            return new Rank(Tier.UNRANKED, null, null);
        }
        Pattern pattern = Pattern.compile("^\\[([IVX]+)\\]\\s(.*)$");
        Matcher matcher = pattern.matcher(stat.subLabel);
        if(matcher.matches()) {
            String romanNumerals = matcher.group(1);
            int division = toArabic(romanNumerals);

            Optional<Tier> first = Arrays.stream(Tier.values()).filter(t -> t.getText().equalsIgnoreCase(matcher.group(2)))
                    .findFirst();
            if(first.isPresent()) {

                return new Rank(first.get(), division, Integer.parseInt(stat.value));
            }
        }

        return null;
    }

    private Stat findStat(List<Stat> stats, String label) {
        if(stats != null) {
            Optional<Stat> first = stats.stream().filter(s -> label.equalsIgnoreCase(s.label)).findFirst();
            if(first.isPresent()) {
                return first.get();
            }
        }
        return null;
    }

    static int toArabic(String  number) {
        if ( "".equalsIgnoreCase(number)) return 0;
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
    @Override
    public String createUrl(String playerId, GamingSystem gamingSystem) {
        return null;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Payload {
        Integer platformId;
        String platformName;
        String platformShortName;
        String platformUserHandle;
        List<Stat> stats;
        @JsonCreator
        public Payload() {
        }

        public Integer getPlatformId() {
            return platformId;
        }

        public void setPlatformId(Integer platformId) {
            this.platformId = platformId;
        }

        public String getPlatformName() {
            return platformName;
        }

        public void setPlatformName(String platformName) {
            this.platformName = platformName;
        }

        public String getPlatformShortName() {
            return platformShortName;
        }

        public void setPlatformShortName(String platformShortName) {
            this.platformShortName = platformShortName;
        }

        public String getPlatformUserHandle() {
            return platformUserHandle;
        }

        public void setPlatformUserHandle(String platformUserHandle) {
            this.platformUserHandle = platformUserHandle;
        }

        public List<Stat> getStats() {
            return stats;
        }

        public void setStats(List<Stat> stats) {
            this.stats = stats;
        }
    }
    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class Stat {
        String label;
        String subLabel;
        String category;
        String value;
        String rank;
        String percentile;
        String displayValue;
        @JsonCreator
        public Stat() {
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getSubLabel() {
            return subLabel;
        }

        public void setSubLabel(String subLabel) {
            this.subLabel = subLabel;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        public String getRank() {
            return rank;
        }

        public void setRank(String rank) {
            this.rank = rank;
        }

        public String getPercentile() {
            return percentile;
        }

        public void setPercentile(String percentile) {
            this.percentile = percentile;
        }

        public String getDisplayValue() {
            return displayValue;
        }

        public void setDisplayValue(String displayValue) {
            this.displayValue = displayValue;
        }
    }
}
