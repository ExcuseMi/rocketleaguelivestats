package com.excuseme.rocketleaguelivestats.scanner.matcher;

import com.excuseme.rocketleaguelivestats.scanner.model.OwnPlayer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OwnPlayerLineMatcher implements LineMatcher<OwnPlayer> {
//[0004.02] DevOnline: Steam ID: 545454546426
    private static final Pattern DEV_ONLINE_STEAM_ID = Pattern.compile("^\\[\\d*\\.?\\d*\\] DevOnline: Steam ID: (.*)$");

    public OwnPlayer match(String line) {
        final Matcher matcher = DEV_ONLINE_STEAM_ID.matcher(line);
        if(matcher.matches()) {
            return new OwnPlayer(matcher.group(1));
        }
        return null;
    }
}
