package com.excuseme.rocketleaguelivestats.scanner;

import com.excuseme.rocketleaguelivestats.scanner.matcher.LoadoutValidationLineMatcher;
import com.excuseme.rocketleaguelivestats.scanner.model.PlayerName;
import org.junit.Test;

import static org.junit.Assert.*;

public class LoadoutValidationLineMatcherTest {
    private final LoadoutValidationLineMatcher loadoutValidationLineMatcher = new LoadoutValidationLineMatcher();

    @Test
    public void test2() {
        String test ="[0092.43] GameEvent: PRI_TA_5 SetGameEvent PlayerName=bo-kw-saleen GameEvent=None InGameEvent=GameEvent_Soccar_TA_0";
        PlayerName match = loadoutValidationLineMatcher.match(test);
        assertNotNull(match);
        assertEquals("bo-kw-saleen", match.getName());
        assertEquals(5, match.getNumber());
    }
}