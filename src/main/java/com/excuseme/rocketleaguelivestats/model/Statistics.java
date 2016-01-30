package com.excuseme.rocketleaguelivestats.model;

public class Statistics {
    private Integer oneVsOne;
    private Integer twoVsTwo;
    private Integer ThreeVSThreeSolo;
    private Integer ThreeVsThree;

    public Integer getOneVsOne() {
        return oneVsOne;
    }

    public Integer getTwoVsTwo() {
        return twoVsTwo;
    }

    public Integer getThreeVSThreeSolo() {
        return ThreeVSThreeSolo;
    }

    public Integer getThreeVsThree() {
        return ThreeVsThree;
    }

    public void setOneVsOne(Integer oneVsOne) {
        this.oneVsOne = oneVsOne;
    }

    public void setTwoVsTwo(Integer twoVsTwo) {
        this.twoVsTwo = twoVsTwo;
    }

    public void setThreeVSThreeSolo(Integer threeVSThreeSolo) {
        ThreeVSThreeSolo = threeVSThreeSolo;
    }

    public void setThreeVsThree(Integer threeVsThree) {
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

    public Integer getAverage() {
        return(int) Math.floor((oneVsOne + twoVsTwo + ThreeVSThreeSolo + ThreeVsThree) /4) ;
    }
}
