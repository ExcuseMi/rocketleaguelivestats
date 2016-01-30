package com.excuseme.rocketleaguelivestats.scanner.matcher;

public class OwnPlayerLineMatcher implements LineMatcher<OwnPlayer> {

    public static final String DEV_ONLINE_STEAM_ID = "DevOnline: Steam ID: ";

    public OwnPlayer match(String line) {
        if(line.contains(DEV_ONLINE_STEAM_ID)) {
            return new OwnPlayer(line.substring(line.indexOf(DEV_ONLINE_STEAM_ID) + DEV_ONLINE_STEAM_ID.length()));
        }
        return null;
    }
}
