package com.excuseme.rocketleaguelivestats.repository;

import com.excuseme.rocketleaguelivestats.model.PlayerIdentifier;
import com.excuseme.rocketleaguelivestats.model.Statistics;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface StatisticsRepository {
    Statistics find(String playerId, String system);

    default Map<PlayerIdentifier,Statistics> find(List<PlayerIdentifier> playerIdentifiers) throws Exception {
        Map<PlayerIdentifier,Statistics> map = new HashMap<>();
        playerIdentifiers.forEach(p->map.put(p, find(p.getPlayerId(), p.getGamingSystem().getQualifier())));
        return map;
    }

    String createUrl(String playerId, String system);
}
