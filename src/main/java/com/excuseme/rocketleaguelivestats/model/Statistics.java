package com.excuseme.rocketleaguelivestats.model;

public class Statistics {
    private Rank oneVsOne;
    private Rank twoVsTwo;
    private Rank ThreeVSThreeSolo;
    private Rank ThreeVsThree;

    public Rank getOneVsOne() {
        return oneVsOne;
    }

    public void setOneVsOne(Rank oneVsOne) {
        this.oneVsOne = oneVsOne;
    }

    public Rank getTwoVsTwo() {
        return twoVsTwo;
    }

    public void setTwoVsTwo(Rank twoVsTwo) {
        this.twoVsTwo = twoVsTwo;
    }

    public Rank getThreeVSThreeSolo() {
        return ThreeVSThreeSolo;
    }

    public void setThreeVSThreeSolo(Rank threeVSThreeSolo) {
        ThreeVSThreeSolo = threeVSThreeSolo;
    }

    public Rank getThreeVsThree() {
        return ThreeVsThree;
    }

    public void setThreeVsThree(Rank threeVsThree) {
        ThreeVsThree = threeVsThree;
    }

    @Override
    public String toString() {
        return "Statistics{" +
                "oneVsOne=" + oneVsOne +
                ", twoVsTwo=" + twoVsTwo +
                ", ThreeVSThreeSolo=" + ThreeVSThreeSolo +
                ", ThreeVsThree=" + ThreeVsThree +
                '}';
    }

}
