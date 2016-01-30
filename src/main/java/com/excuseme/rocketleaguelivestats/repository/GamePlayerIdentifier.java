package com.excuseme.rocketleaguelivestats.repository;

import com.excuseme.rocketleaguelivestats.model.PlayerIdentifier;

public class GamePlayerIdentifier {
    private final String gameIdentifier;
    private final PlayerIdentifier playerIdentifier;

    public GamePlayerIdentifier(String gameIdentifier, PlayerIdentifier playerIdentifier) {
        this.gameIdentifier = gameIdentifier;
        this.playerIdentifier = playerIdentifier;
    }

    public String getGameIdentifier() {
        return gameIdentifier;
    }

    public PlayerIdentifier getPlayerIdentifier() {
        return playerIdentifier;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        GamePlayerIdentifier that = (GamePlayerIdentifier) o;

        if (gameIdentifier != null ? !gameIdentifier.equals(that.gameIdentifier) : that.gameIdentifier != null)
            return false;
        return playerIdentifier != null ? playerIdentifier.equals(that.playerIdentifier) : that.playerIdentifier == null;

    }

    @Override
    public int hashCode() {
        int result = gameIdentifier != null ? gameIdentifier.hashCode() : 0;
        result = 31 * result + (playerIdentifier != null ? playerIdentifier.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "GamePlayerIdentifier{" +
                "gameIdentifier='" + gameIdentifier + '\'' +
                ", playerIdentifier=" + playerIdentifier +
                '}';
    }
}
