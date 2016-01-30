package com.excuseme.rocketleaguelivestats.scanner.model;

public class NamePlate {
    private String name;
    private int row;

    public NamePlate(String name, int row) {
        this.name = name;
        this.row = row;
    }

    public String getName() {
        return name;
    }

    public int getRow() {
        return row;
    }

    @Override
    public String toString() {
        return "NamePlate{" +
                "name='" + name + '\'' +
                ", row=" + row +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NamePlate namePlate = (NamePlate) o;

        return name != null ? name.equals(namePlate.name) : namePlate.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
