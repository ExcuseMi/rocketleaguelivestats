package com.excuseme.rocketleaguelivestats.scanner.model;

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

    private GameData(GameData gameData) {
        this.identifier = gameData.identifier;
        namePlates = gameData.namePlates;
        playerNames = gameData.playerNames;
        playerIds = gameData.playerIds;
        playerRemoveds = gameData.playerRemoveds;
        gameType = gameData.gameType;
        ended = gameData.ended;
        ownPlayer = gameData.ownPlayer;
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
        if (o == null || getClass() != o.getClass()) return false;

        GameData gameData = (GameData) o;

        if (ended != gameData.ended) return false;
        if (identifier != null ? !identifier.equals(gameData.identifier) : gameData.identifier != null) return false;
        if (namePlates != null ? !namePlates.equals(gameData.namePlates) : gameData.namePlates != null) return false;
        if (playerNames != null ? !playerNames.equals(gameData.playerNames) : gameData.playerNames != null)
            return false;
        if (playerIds != null ? !playerIds.equals(gameData.playerIds) : gameData.playerIds != null) return false;
        if (playerRemoveds != null ? !playerRemoveds.equals(gameData.playerRemoveds) : gameData.playerRemoveds != null)
            return false;
        return gameType != null ? gameType.equals(gameData.gameType) : gameData.gameType == null;

    }

    @Override
    public int hashCode() {
        int result = identifier != null ? identifier.hashCode() : 0;
        result = 31 * result + (namePlates != null ? namePlates.hashCode() : 0);
        result = 31 * result + (playerNames != null ? playerNames.hashCode() : 0);
        result = 31 * result + (playerIds != null ? playerIds.hashCode() : 0);
        result = 31 * result + (playerRemoveds != null ? playerRemoveds.hashCode() : 0);
        result = 31 * result + (ended ? 1 : 0);
        result = 31 * result + (gameType != null ? gameType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GameData{" +
                "identifier='" + identifier + '\'' +
                ", namePlates=" + namePlates +
                ", playerNames=" + playerNames +
                ", playerIds=" + playerIds +
                ", playerRemoveds=" + playerRemoveds +
                ", ended=" + ended +
                ", gameType=" + gameType +
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

    public GameData clone() {
        return new GameData(this);
    }

    public OwnPlayer getOwnPlayer() {
        return ownPlayer;
    }

    public void setOwnPlayer(OwnPlayer ownPlayer) {
        this.ownPlayer = ownPlayer;
    }
}
