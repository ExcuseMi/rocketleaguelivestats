package com.excuseme.rocketleaguelivestats.model;

public class Player {
    private int number;
    private int row;
    private String name;
    private PlayerIdentifier playerIdentifier;
    private boolean active;
    private Statistics statistics;
    private boolean ownPlayer;

    public Player(int number, int row, String name, PlayerIdentifier playerIdentifier, boolean active, boolean ownPlayer) {
        this.number = number;
        this.row = row;
        this.name = name;
        this.playerIdentifier = playerIdentifier;
        this.active = active;
        this.ownPlayer = ownPlayer;
    }

    public int getRow() {
        return row;
    }

    public String getName() {
        return name;
    }

    public PlayerIdentifier getPlayerIdentifier() {
        return playerIdentifier;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Player player = (Player) o;

        if (active != player.active) return false;
        if (name != null ? !name.equals(player.name) : player.name != null) return false;
        return playerIdentifier != null ? playerIdentifier.equals(player.playerIdentifier) : player.playerIdentifier == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (playerIdentifier != null ? playerIdentifier.hashCode() : 0);
        result = 31 * result + (active ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Player{" +
                "number=" + number +
                ", row=" + row +
                ", name='" + name + '\'' +
                ", playerIdentifier=" + playerIdentifier +
                ", statistics=" + statistics +
                '}';
    }

    public boolean isOwnPlayer() {
        return ownPlayer;
    }

}
