package com.excuseme.rocketleaguelivestats.scanner.model;

import com.excuseme.rocketleaguelivestats.scanner.matcher.OwnPlayer;

import java.util.HashSet;
import java.util.Set;

public class GameData {
    private String identifier;
    private Set<NamePlate> namePlates;
    private Set<PlayerName> playerNames;
    private Set<PlayerId> playerIds;
    private Set<HandlePlayerRemoved> playerRemoveds;
    private boolean ended;
    private GameType gameType;
    private OwnPlayer ownPlayer;

    public GameData(String identifier) {
        this.identifier = identifier;
        namePlates = new HashSet<NamePlate>();
        playerNames = new HashSet<PlayerName>();
        playerIds = new HashSet<PlayerId>();
        playerRemoveds = new HashSet<HandlePlayerRemoved>();

        ended = false;
    }

    public void addNamePlate(NamePlate namePlate) {
        if(!ended) {
            if (namePlates.contains(namePlate)) {
                namePlates.remove(namePlate);
            }
            namePlates.add(namePlate);
        }
    }

    public void addPlayerName(PlayerName playerName) {
        if(!ended) {
            if (playerNames.contains(playerName)) {
                playerNames.remove(playerName);
            }
            playerNames.add(playerName);
        }
    }

    public void addPlayerId(PlayerId playerId) {
        if(!ended) {
            if (playerIds.contains(playerId)) {
                playerIds.remove(playerId);
            }
            playerIds.add(playerId);
        }
    }

    public String getIdentifier() {
        return identifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameData gameData = (GameData) o;

        return identifier != null ? identifier.equals(gameData.identifier) : gameData.identifier == null;

    }

    @Override
    public int hashCode() {
        return identifier != null ? identifier.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "GameData{" +
                "identifier='" + identifier + '\'' +
                ", gameType=" + gameType +
                ", namePlates=" + namePlates +
                ", playerNames=" + playerNames +
                ", playerIds=" + playerIds +
                ", playerRemoveds=" + playerRemoveds +
                ", ended=" + ended +
                '}';
    }

    public Set<NamePlate> getNamePlates() {
        return namePlates;
    }

    public Set<PlayerName> getPlayerNames() {
        return playerNames;
    }

    public Set<PlayerId> getPlayerIds() {
        return playerIds;
    }

    public Set<HandlePlayerRemoved> getPlayerRemoveds() {
        return playerRemoveds;
    }

    public boolean isEmpty() {
        return namePlates.isEmpty() || playerNames.isEmpty() || playerIds.isEmpty();
    }


    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public void addPlayerRemoved(HandlePlayerRemoved handlePlayerRemoved) {
        if(!ended) {
            if (playerRemoveds.contains(handlePlayerRemoved)) {
                playerRemoveds.remove(handlePlayerRemoved);
            }
            playerRemoveds.add(handlePlayerRemoved);
        }

    }

    public void setGameType(GameType gameType) {
        this.gameType = gameType;
    }

    public GameType getGameType() {
        return gameType;
    }

    public void setOwnPlayer(OwnPlayer ownPlayer) {
        this.ownPlayer = ownPlayer;
    }

    public OwnPlayer getOwnPlayer() {
        return ownPlayer;
    }
}
