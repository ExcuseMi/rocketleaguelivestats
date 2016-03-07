package com.excuseme.rocketleaguelivestats.scanner.matcher;

import org.junit.Test;

import static org.junit.Assert.*;

public class BuildIdLineMatcherTest {

    @Test
    public void testMatch() throws Exception {
        final BuildIdLineMatcher buildIdLineMatcher = new BuildIdLineMatcher();
        final String match = buildIdLineMatcher.match("[0003.04] Matchmaking: Build Unique ID: -1543484724");
        assertEquals("-1543484724", match);
    }
}