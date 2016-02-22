package com.excuseme.rocketleaguelivestats.repository;

import com.excuseme.rocketleaguelivestats.model.PlayerIdentifier;
import com.excuseme.rocketleaguelivestats.model.Statistics;

import java.util.HashMap;
import java.util.Map;

public class CachedStatisticsRepository {

    private final StatisticsRepository statisticsRepository;
    private Map<GamePlayerIdentifier, Statistics> gamePlayerIdentifierStatisticsMap = new HashMap<GamePlayerIdentifier, Statistics>();

    public CachedStatisticsRepository() {
        statisticsRepository = new StatisticsRepository(new RankParser());
    }

    public Statistics find(String gameIdentifier, PlayerIdentifier playerIdentifier)  {
        final GamePlayerIdentifier gamePlayerIdentifier = new GamePlayerIdentifier(gameIdentifier, playerIdentifier);
        if(!gamePlayerIdentifierStatisticsMap.containsKey(gamePlayerIdentifier)) {
            Statistics statistics = statisticsRepository.find(playerIdentifier.getPlayerId(), playerIdentifier.getGamingSystem().getQualifier());
            if(statistics == null) {
                statistics = statisticsRepository.find(playerIdentifier.getPlayerId(), playerIdentifier.getGamingSystem().getQualifier());
            }
            gamePlayerIdentifierStatisticsMap.put(gamePlayerIdentifier, statistics);
        }
        return gamePlayerIdentifierStatisticsMap.get(gamePlayerIdentifier);
    }

}
