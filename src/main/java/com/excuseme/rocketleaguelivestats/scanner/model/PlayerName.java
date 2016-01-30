package com.excuseme.rocketleaguelivestats.scanner.model;

public class PlayerName {
    private int number;
    private String name;

    public PlayerName(int number, String name) {
        this.number = number;
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerName that = (PlayerName) o;

        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "PlayerName{" +
                "number=" + number +
                ", name='" + name + '\'' +
                '}';
    }
}
