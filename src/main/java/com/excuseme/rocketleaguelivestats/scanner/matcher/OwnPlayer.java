package com.excuseme.rocketleaguelivestats.scanner.matcher;

public class OwnPlayer {

    private String steamId;

    public OwnPlayer(String steamId) {
        this.steamId = steamId;
    }

    public String getSteamId() {
        return steamId;
    }

    @Override
    public String toString() {
        return "OwnPlayer{" +
                "steamId='" + steamId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OwnPlayer ownPlayer = (OwnPlayer) o;

        return steamId != null ? steamId.equals(ownPlayer.steamId) : ownPlayer.steamId == null;

    }

    @Override
    public int hashCode() {
        return steamId != null ? steamId.hashCode() : 0;
    }
}
