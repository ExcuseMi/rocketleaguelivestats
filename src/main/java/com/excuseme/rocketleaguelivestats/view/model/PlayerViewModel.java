package com.excuseme.rocketleaguelivestats.view.model;

import com.excuseme.rocketleaguelivestats.model.Player;
import com.excuseme.rocketleaguelivestats.model.PlayerIdentifier;
import com.excuseme.rocketleaguelivestats.model.Rank;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

public class PlayerViewModel {
    private final SimpleStringProperty name;
    private final SimpleStringProperty system;
    private final SimpleObjectProperty<Rank> oneVsOne;
    private final SimpleObjectProperty<Rank> twoVsTwo;
    private final SimpleObjectProperty<Rank> threeVsThreeSolo;
    private final SimpleObjectProperty<Rank> threeVsThreeStandard;
    private final PlayerIdentifier playerIdentifier;
    private final SimpleBooleanProperty active;
    private final boolean ownPlayer;

    public PlayerViewModel(Player player) {
        name = new SimpleStringProperty(player.getName());
        system = new SimpleStringProperty(player.getPlayerIdentifier().getGamingSystem().name());
        oneVsOne = new SimpleObjectProperty<Rank>();
        twoVsTwo = new SimpleObjectProperty<Rank>();
        threeVsThreeSolo = new SimpleObjectProperty<Rank>();
        threeVsThreeStandard = new SimpleObjectProperty<Rank>();
        active = new SimpleBooleanProperty(player.isActive());
        this.playerIdentifier = player.getPlayerIdentifier();
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

    public Rank getOneVsOne() {
        return oneVsOne.get();
    }

    public SimpleObjectProperty<Rank> oneVsOneProperty() {
        return oneVsOne;
    }

    public void setOneVsOne(Rank oneVsOne) {
        this.oneVsOne.set(oneVsOne);
    }

    public Rank getTwoVsTwo() {
        return twoVsTwo.get();
    }

    public SimpleObjectProperty<Rank> twoVsTwoProperty() {
        return twoVsTwo;
    }

    public void setTwoVsTwo(Rank twoVsTwo) {
        this.twoVsTwo.set(twoVsTwo);
    }

    public Rank getThreeVsThreeSolo() {
        return threeVsThreeSolo.get();
    }

    public SimpleObjectProperty<Rank> threeVsThreeSoloProperty() {
        return threeVsThreeSolo;
    }

    public void setThreeVsThreeSolo(Rank threeVsThreeSolo) {
        this.threeVsThreeSolo.set(threeVsThreeSolo);
    }

    public Rank getThreeVsThreeStandard() {
        return threeVsThreeStandard.get();
    }

    public SimpleObjectProperty<Rank> threeVsThreeStandardProperty() {
        return threeVsThreeStandard;
    }

    public void setThreeVsThreeStandard(Rank threeVsThreeStandard) {
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
