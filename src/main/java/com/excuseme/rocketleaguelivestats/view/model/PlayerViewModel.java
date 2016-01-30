package com.excuseme.rocketleaguelivestats.view.model;

import com.excuseme.rocketleaguelivestats.model.Player;
import com.excuseme.rocketleaguelivestats.model.PlayerIdentifier;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class PlayerViewModel {
    private final SimpleStringProperty name;
    private final SimpleStringProperty system;
    private final SimpleIntegerProperty oneVsOne;
    private final SimpleIntegerProperty twoVsTwo;
    private final SimpleIntegerProperty threeVsThreeSolo;
    private final SimpleIntegerProperty threeVsThreeStandard;
    private final PlayerIdentifier playerIdentifier;
    private final SimpleBooleanProperty active;
    private final SimpleIntegerProperty average;
    private final boolean ownPlayer;

    public PlayerViewModel(Player player) {
        name = new SimpleStringProperty(player.getName());
        system = new SimpleStringProperty(player.getPlayerIdentifier().getGamingSystem().name());
        oneVsOne = new SimpleIntegerProperty();
        twoVsTwo = new SimpleIntegerProperty();
        threeVsThreeSolo = new SimpleIntegerProperty();
        threeVsThreeStandard = new SimpleIntegerProperty();
        active = new SimpleBooleanProperty(player.isActive());
        this.playerIdentifier = player.getPlayerIdentifier();
        average = new SimpleIntegerProperty();
        ownPlayer = player.isOwnPlayer();

    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSystem() {
        return system.get();
    }

    public SimpleStringProperty systemProperty() {
        return system;
    }

    public void setSystem(String system) {
        this.system.set(system);
    }

    public Integer getOneVsOne() {
        return oneVsOne.get();
    }

    public SimpleIntegerProperty oneVsOneProperty() {
        return oneVsOne;
    }

    public void setOneVsOne(Integer oneVsOne) {
        this.oneVsOne.set(oneVsOne);
    }

    public Integer getTwoVsTwo() {
        return twoVsTwo.get();
    }

    public SimpleIntegerProperty twoVsTwoProperty() {
        return twoVsTwo;
    }

    public void setTwoVsTwo(Integer twoVsTwo) {
        this.twoVsTwo.set(twoVsTwo);
    }

    public Integer getThreeVsThreeSolo() {
        return threeVsThreeSolo.get();
    }

    public SimpleIntegerProperty threeVsThreeSoloProperty() {
        return threeVsThreeSolo;
    }

    public void setThreeVsThreeSolo(Integer threeVsThreeSolo) {
        this.threeVsThreeSolo.set(threeVsThreeSolo);
    }

    public Integer getThreeVsThreeStandard() {
        return threeVsThreeStandard.get();
    }

    public SimpleIntegerProperty threeVsThreeStandardProperty() {
        return threeVsThreeStandard;
    }

    public void setThreeVsThreeStandard(Integer threeVsThreeStandard) {
        this.threeVsThreeStandard.set(threeVsThreeStandard);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PlayerViewModel that = (PlayerViewModel) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (system != null ? !system.equals(that.system) : that.system != null) return false;
        if (playerIdentifier != null ? !playerIdentifier.equals(that.playerIdentifier) : that.playerIdentifier != null)
            return false;
        return active != null ? active.equals(that.active) : that.active == null;

    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (system != null ? system.hashCode() : 0);
        result = 31 * result + (playerIdentifier != null ? playerIdentifier.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return name.get() +
                "[1v1=" + oneVsOne.get() +
                ",2v2=" + twoVsTwo.get() +
                ",3v3 Solo=" + threeVsThreeSolo.get() +
                ",3v3=" + threeVsThreeStandard.get() +
                '}';
    }

    public Integer getAverage() {
        return average.get();
    }

    public SimpleIntegerProperty averageProperty() {
        return average;
    }

    public void setAverage(Integer average) {
        this.average.set(average);
    }

    public PlayerIdentifier getPlayerIdentifier() {
        return playerIdentifier;
    }

    public boolean getActive() {
        return active.get();
    }

    public SimpleBooleanProperty activeProperty() {
        return active;
    }

    public void setActive(boolean active) {
        this.active.set(active);
    }

    public boolean isOwnPlayer() {
        return ownPlayer;
    }
}
