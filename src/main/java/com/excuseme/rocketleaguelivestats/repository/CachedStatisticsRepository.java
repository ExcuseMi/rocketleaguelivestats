package com.excuseme.rocketleaguelivestats.repository;

import com.excuseme.rocketleaguelivestats.model.PlayerIdentifier;
import com.excuseme.rocketleaguelivestats.model.Statistics;
import com.excuseme.rocketleaguelivestats.repository.rocketleague.RocketLeagueAPI;
import com.excuseme.rocketleaguelivestats.repository.rocketleaguestats.RankParser;
import com.excuseme.rocketleaguelivestats.repository.rocketleaguestats.RocketLeagueStatsStatisticsRepository;
import com.excuseme.rocketleaguelivestats.repository.rocketleaguetracker.RocketLeagueTrackerStatisticsRepository;
import com.excuseme.rocketleaguelivestats.scanner.model.SessionData;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CachedStatisticsRepository {

    private StatisticsRepository statisticsRepository;
    private Map<GamePlayerIdentifier, Statistics> gamePlayerIdentifierStatisticsMap = new HashMap<GamePlayerIdentifier, Statistics>();

    public CachedStatisticsRepository() {
//        statisticsRepository = new RocketLeagueStatsStatisticsRepository(new RankParser());
        statisticsRepository = new RocketLeagueTrackerStatisticsRepository();
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

    public Map<PlayerIdentifier, Statistics> findAll(String gameIdentifier, List<PlayerIdentifier> playerIdentifierList) throws Exception {
        final List<GamePlayerIdentifier> gamePlayerIdentifiers = playerIdentifierList.stream().map(p -> new GamePlayerIdentifier(gameIdentifier, p)).collect(Collectors.toList());

        final List<GamePlayerIdentifier> notKnownStatistics = gamePlayerIdentifiers.stream().filter(g -> !gamePlayerIdentifierStatisticsMap.containsKey(g)).collect(Collectors.toList());
        final Map<PlayerIdentifier, Statistics> playerIdentifierStatisticsMap = statisticsRepository.find(notKnownStatistics.stream().map(GamePlayerIdentifier::getPlayerIdentifier).collect(Collectors.toList()));
        playerIdentifierStatisticsMap.entrySet().forEach(e->gamePlayerIdentifierStatisticsMap.put(new GamePlayerIdentifier(gameIdentifier, e.getKey()), e.getValue()));
        final Map<PlayerIdentifier, Statistics> results = new HashMap<>();
        gamePlayerIdentifiers.forEach(g->results.put(g.getPlayerIdentifier(), gamePlayerIdentifierStatisticsMap.get(g)));
        return results;
    }

    public String createUrl(PlayerIdentifier playerIdentifier) {
        return statisticsRepository.createUrl(playerIdentifier.getPlayerId(), playerIdentifier.getGamingSystem().getQualifier());
    }

    public void updateSessionData(SessionData sessionData) {
        statisticsRepository = new RocketLeagueAPI(sessionData);

    }
}
