package com.excuseme.rocketleaguelivestats.scanner.model;

public class GameType {
    private String mapName;
    private Integer playlistType;
    private String gameDescription;

    public GameType(String mapName, Integer playlistType, String gameDescription) {
        this.mapName = mapName;
        this.playlistType = playlistType;
        this.gameDescription = gameDescription;
    }

    public String getMapName() {
        return mapName;
    }

    public Integer getPlaylistType() {
        return playlistType;
    }

    public String getGameDescription() {
        return gameDescription;
    }

    @Override
    public String toString() {
        return "GameType{" +
                "mapName='" + mapName + '\'' +
                ", playlistType=" + playlistType +
                ", gameDescription='" + gameDescription + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GameType gameType = (GameType) o;

        if (mapName != null ? !mapName.equals(gameType.mapName) : gameType.mapName != null) return false;
        if (playlistType != null ? !playlistType.equals(gameType.playlistType) : gameType.playlistType != null)
            return false;
        return gameDescription != null ? gameDescription.equals(gameType.gameDescription) : gameType.gameDescription == null;

    }

    @Override
    public int hashCode() {
        int result = mapName != null ? mapName.hashCode() : 0;
        result = 31 * result + (playlistType != null ? playlistType.hashCode() : 0);
        result = 31 * result + (gameDescription != null ? gameDescription.hashCode() : 0);
        return result;
    }
}
