package com.excuseme.rocketleaguelivestats.scanner.matcher;

import com.excuseme.rocketleaguelivestats.scanner.model.HandlePlayerRemoved;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HandlePlayerRemovedLineMatcher implements LineMatcher<HandlePlayerRemoved> {

    private static final Pattern NAMEPLATE_HANDLE_PLAYER_REMOVED = Pattern.compile("^\\[\\d*\\.?\\d*\\] Nameplate: HandlePlayerRemoved (.*)$");

    public HandlePlayerRemoved match(String line) {
        final Matcher matcher = NAMEPLATE_HANDLE_PLAYER_REMOVED.matcher(line);
        if(matcher.matches()) {
            return new HandlePlayerRemoved(matcher.group(1));
        }
        return null;
    }
}
