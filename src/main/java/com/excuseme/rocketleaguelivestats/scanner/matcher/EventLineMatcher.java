package com.excuseme.rocketleaguelivestats.scanner.matcher;

import com.excuseme.rocketleaguelivestats.scanner.model.Event;

public class EventLineMatcher implements LineMatcher<Event>{

    public static final String LOG_MAP_LOAD_SPAWN_PLAYERS = "Log: Bringing up level for play took";
    public static final String ENDED_1 = "GameEvent: GotoGameState GameEvent_Soccar GameEvent_Soccar_TA_";
    public static final String ENDED_2 = "Finished";

    public Event match(String line) {
        if(line.contains(LOG_MAP_LOAD_SPAWN_PLAYERS)) {
            String identifier = line.substring(1, line.indexOf("]"));
            return new Event(identifier, Event.EventType.GAME_LOADED);
        } else if(line.contains(ENDED_1) && line.contains(ENDED_2) ) {
            String identifier = line.substring(1, line.indexOf("]"));
            return new Event(identifier, Event.EventType.GAME_ENDED);
        }
        return null;
    }
}
