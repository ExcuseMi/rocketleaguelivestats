package com.excuseme.rocketleaguelivestats.scanner.matcher;

public class BuildIdLineMatcher implements LineMatcher<String> {

    public static final String MATCHMAKING_BUILD_UNIQUE_ID = "Matchmaking: Build Unique ID: ";

    //[0003.04] Matchmaking: Build Unique ID: -1543484724
    @Override
    public String match(String line) {
        if(line.contains(MATCHMAKING_BUILD_UNIQUE_ID)) {
            return line.substring(line.indexOf(MATCHMAKING_BUILD_UNIQUE_ID) + MATCHMAKING_BUILD_UNIQUE_ID.length());
        }
        return null;
    }
}
