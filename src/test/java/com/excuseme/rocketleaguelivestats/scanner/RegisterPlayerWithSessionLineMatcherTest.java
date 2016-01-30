package com.excuseme.rocketleaguelivestats.scanner;

import com.excuseme.rocketleaguelivestats.scanner.matcher.RegisterPlayerWithSessionLineMatcher;
import com.excuseme.rocketleaguelivestats.scanner.model.PlayerId;
import org.junit.Test;

import static org.junit.Assert.*;

public class RegisterPlayerWithSessionLineMatcherTest {

    private final RegisterPlayerWithSessionLineMatcher registerPlayerWithSessionLineMatcher = new RegisterPlayerWithSessionLineMatcher();

    @Test
    public void testPS4() {
        String ps4Line = "[0092.43] ScriptLog: PRI_TA_15 RegisterPlayerWithSession PS4-bo-kw-saleen-0 Game\n";
        PlayerId playerId = registerPlayerWithSessionLineMatcher.match(ps4Line);
        assertNotNull(playerId);
        assertEquals(15, playerId.getNumber());
        assertEquals("bo-kw-saleen", playerId.getId());
        assertEquals("PS4", playerId.getSystem());


    }

    @Test
    public void testSteam() {
       String steamLine = "[0050.43] ScriptLog: PRI_TA_2 RegisterPlayerWithSession Steam-76561197962717112-0 Game";
        PlayerId playerId = registerPlayerWithSessionLineMatcher.match(steamLine);
        assertNotNull(playerId);
        assertEquals("76561197962717112", playerId.getId());
        assertEquals("Steam", playerId.getSystem());
    }
}