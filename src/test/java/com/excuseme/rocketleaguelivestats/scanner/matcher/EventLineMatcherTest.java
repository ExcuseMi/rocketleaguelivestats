package com.excuseme.rocketleaguelivestats.scanner.matcher;

import com.excuseme.rocketleaguelivestats.scanner.model.Event;
import org.junit.Test;

import static org.junit.Assert.*;

public class EventLineMatcherTest {
    @Test
    public void test() {
        EventLineMatcher eventLineMatcher = new EventLineMatcher();
        Event match = eventLineMatcher.match("[0115.72] Log: Bringing up level for play took");
        assertNotNull(match);
        String identifier = match.getIdentifier();
        assertEquals("0115.72", identifier);
    }
}