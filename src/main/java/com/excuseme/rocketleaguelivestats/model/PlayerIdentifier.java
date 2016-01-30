package com.excuseme.rocketleaguelivestats.model;

public class PlayerIdentifier {
    private String playerId;
    private GamingSystem gamingSystem;

    public PlayerIdentifier(String playerId, GamingSystem gamingSystem) {
        this.playerId = playerId;
        this.gamingSystem = gamingSystem;
    }

    public String getPlayerId() {
        return playerId;
    }

    public GamingSystem getGamingSystem() {
        return gamingSystem;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerIdentifier that = (PlayerIdentifier) o;

        if (playerId != null ? !playerId.equals(that.playerId) : that.playerId != null) return false;
        return gamingSystem == that.gamingSystem;

    }

    @Override
    public int hashCode() {
        int result = playerId != null ? playerId.hashCode() : 0;
        result = 31 * result + (gamingSystem != null ? gamingSystem.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PlayerIdentifier{" +
                "playerId='" + playerId + '\'' +
                ", gamingSystem=" + gamingSystem +
                '}';
    }
}
