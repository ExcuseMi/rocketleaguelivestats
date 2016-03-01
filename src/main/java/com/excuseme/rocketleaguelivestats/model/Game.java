package com.excuseme.rocketleaguelivestats.model;

import java.util.List;

public class Game {
    private String identifier;
    private List<Player> players;
    private String map;
    private Playlist playlist;

    public Game(String identifier, List<Player> players, String map, Playlist playlist) {
        this.identifier = identifier;
        this.players = players;
        this.map = map;
        this.playlist = playlist;
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public String getMap() {
        return map;
    }

    public Playlist getPlaylist() {
        return playlist;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Game game = (Game) o;

        if (identifier != null ? !identifier.equals(game.identifier) : game.identifier != null) return false;
        return players != null ? players.equals(game.players) : game.players == null;

    }

    @Override
    public int hashCode() {
        int result = identifier != null ? identifier.hashCode() : 0;
        result = 31 * result + (players != null ? players.hashCode() : 0);
        return result;
    }
}
