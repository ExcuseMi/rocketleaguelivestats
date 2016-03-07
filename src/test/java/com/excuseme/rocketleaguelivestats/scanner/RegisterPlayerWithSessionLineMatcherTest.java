package com.excuseme.rocketleaguelivestats.scanner;

import com.excuseme.rocketleaguelivestats.scanner.matcher.RegisterPlayerWithSessionLineMatcher;
import com.excuseme.rocketleaguelivestats.scanner.model.PlayerId;
import org.junit.Test;

import static org.junit.Assert.*;

public class RegisterPlayerWithSessionLineMatcherTest {

    private final RegisterPlayerWithSessionLineMatcher registerPlayerWithSessionLineMatcher = new RegisterPlayerWithSessionLineMatcher();

    @Test
    public void testPS4() {
        String ps4Line = "[6570.16] ScriptLog: PRI_TA_67 RegisterPlayerWithSession PS4|assainskid1|0 Game";
        PlayerId playerId = registerPlayerWithSessionLineMatcher.match(ps4Line);
        assertNotNull(playerId);
        assertEquals(67, playerId.getNumber());
        assertEquals("assainskid1", playerId.getId());
        assertEquals("PS4", playerId.getSystem());


    }

    //[1945.61] ScriptLog: PRI_TA_18 RegisterPlayerWithSession Steam|76561197962717112|0 Game
    @Test
    public void testNewSteam() {
        String steamLine = "[1945.61] ScriptLog: PRI_TA_18 RegisterPlayerWithSession Steam|76561197962717112|0 Game";
        PlayerId playerId = registerPlayerWithSessionLineMatcher.match(steamLine);
        assertNotNull(playerId);
        assertEquals("76561197962717112", playerId.getId());
        assertEquals("Steam", playerId.getSystem());
    }


}