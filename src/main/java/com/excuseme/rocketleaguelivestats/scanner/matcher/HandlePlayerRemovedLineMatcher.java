package com.excuseme.rocketleaguelivestats.scanner.matcher;

import com.excuseme.rocketleaguelivestats.scanner.model.HandlePlayerRemoved;

public class HandlePlayerRemovedLineMatcher implements LineMatcher<HandlePlayerRemoved> {

    public static final String NAMEPLATE_HANDLE_PLAYER_REMOVED = "Nameplate: HandlePlayerRemoved ";

    public HandlePlayerRemoved match(String line) {
        if(line.contains(NAMEPLATE_HANDLE_PLAYER_REMOVED)) {
            return new HandlePlayerRemoved(line.substring(line.indexOf(NAMEPLATE_HANDLE_PLAYER_REMOVED) + NAMEPLATE_HANDLE_PLAYER_REMOVED.length()));
        }
        return null;
    }
}
