package com.excuseme.rocketleaguelivestats.scanner.matcher;

import com.excuseme.rocketleaguelivestats.scanner.model.Event;
import org.junit.Test;

import static org.junit.Assert.*;

public class EventLineMatcherTest {
    @Test
    public void test() {
        EventLineMatcher eventLineMatcher = new EventLineMatcher();
        Event match = eventLineMatcher.match("[0515.43] Log: Bringing up level for play took: 0.069004");
        assertNotNull(match);
        String identifier = match.getIdentifier();
        assertEquals("0515.43", identifier);
        assertEquals(Event.EventType.GAME_LOADED, match.getEventType());

        match = eventLineMatcher.match("[9748.32] GameEvent: GotoGameState GameEvent_Soccar GameEvent_Soccar_TA_20 Finished");
        assertNotNull(match);
        identifier = match.getIdentifier();
        assertEquals("9748.32", identifier);
        assertEquals(Event.EventType.GAME_ENDED, match.getEventType());
    }
}