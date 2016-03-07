package com.excuseme.rocketleaguelivestats.scanner.matcher;

import com.excuseme.rocketleaguelivestats.scanner.model.Event;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EventLineMatcher implements LineMatcher<Event>{

    public static final Pattern LOG_MAP_LOAD_SPAWN_PLAYERS_PATTERN = Pattern.compile("^\\[(\\d*\\.?\\d*)\\] Log: Bringing up level for play took: \\d*\\.?\\d*$");
    public static final Pattern ENDED_PATTERN = Pattern.compile("^\\[(\\d*\\.?\\d*)\\] GameEvent: GotoGameState GameEvent_Soccar GameEvent_Soccar_TA_\\d* Finished$");

    public Event match(String line) {
        final Matcher startGameMatcher = LOG_MAP_LOAD_SPAWN_PLAYERS_PATTERN.matcher(line);
        final Matcher endGameMatcher = ENDED_PATTERN.matcher(line);

        if(startGameMatcher.matches()) {
            return new Event(startGameMatcher.group(1), Event.EventType.GAME_LOADED);
        } else if(endGameMatcher.matches() ) {
            String identifier = endGameMatcher.group(1);
            return new Event(identifier, Event.EventType.GAME_ENDED);
        }
        return null;
    }
}
