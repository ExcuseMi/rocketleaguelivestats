package com.excuseme.rocketleaguelivestats.scanner.matcher;

import com.excuseme.rocketleaguelivestats.scanner.model.HandlePlayerRemoved;
import org.junit.Test;

import static org.junit.Assert.*;

public class HandlePlayerRemovedLineMatcherTest {

    @Test
    public void testMatch() throws Exception {
        final HandlePlayerRemovedLineMatcher handlePlayerRemovedLineMatcher = new HandlePlayerRemovedLineMatcher();
        final HandlePlayerRemoved match = handlePlayerRemovedLineMatcher.match("[0990.93] Nameplate: HandlePlayerRemoved Proofbullet");
        assertNotNull(match);
        assertEquals("Proofbullet", match.getName());
    }
}