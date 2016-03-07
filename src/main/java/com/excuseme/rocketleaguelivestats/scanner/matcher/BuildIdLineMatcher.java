package com.excuseme.rocketleaguelivestats.scanner.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BuildIdLineMatcher implements LineMatcher<String> {

    public static final Pattern MATCHMAKING_BUILD_UNIQUE_ID = Pattern.compile("^\\[\\d*\\.?\\d*\\] Matchmaking: Build Unique ID: (-?\\d*\\.{0,1}\\d+)$");

    //[0003.04] Matchmaking: Build Unique ID: -1543484724
    @Override
    public String match(String line) {
        final Matcher matcher = MATCHMAKING_BUILD_UNIQUE_ID.matcher(line);
        if(matcher.matches()) {
            return matcher.group(1);
        }
        return null;
    }
}
