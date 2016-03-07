package com.excuseme.rocketleaguelivestats.scanner.matcher;

import com.excuseme.rocketleaguelivestats.scanner.model.PlayerId;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterPlayerWithSessionLineMatcher implements LineMatcher<PlayerId> {
//[1945.61] ScriptLog: PRI_TA_18 RegisterPlayerWithSession Steam|76561197962717112|0 Game

    private static final Pattern REGISTER_PLAYER_WITH_SESSION = Pattern.compile("^\\[\\d*\\.?\\d*\\] ScriptLog: PRI_TA_(\\d*) RegisterPlayerWithSession (.*?)\\|(.*?)\\|\\d* Game$");

    public PlayerId match(String line) {
        final Matcher matcher = REGISTER_PLAYER_WITH_SESSION.matcher(line);
        if(matcher.matches()) {
            return new PlayerId(Integer.parseInt(matcher.group(1)), matcher.group(3), matcher.group(2));
        }
        return null;
    }
}
