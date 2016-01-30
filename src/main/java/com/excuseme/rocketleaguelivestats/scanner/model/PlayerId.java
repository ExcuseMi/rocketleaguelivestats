package com.excuseme.rocketleaguelivestats.scanner.model;

public class PlayerId {
    private int number;
    private String id;
    private String system;

    public PlayerId(int number, String id, String system) {
        this.number = number;
        this.id = id;
        this.system = system;
    }

    public int getNumber() {
        return number;
    }

    public String getId() {
        return id;
    }

    public String getSystem() {
        return system;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerId playerId = (PlayerId) o;

        if (id != null ? !id.equals(playerId.id) : playerId.id != null) return false;
        return system != null ? system.equals(playerId.system) : playerId.system == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (system != null ? system.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "PlayerId{" +
                "number=" + number +
                ", id='" + id + '\'' +
                ", system='" + system + '\'' +
                '}';
    }
}
