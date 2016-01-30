package com.excuseme.rocketleaguelivestats.scanner.matcher;

import org.junit.Test;

import static org.junit.Assert.*;

public class OwnPlayerLineMatcherTest {

    @Test
    public void test() {
        final OwnPlayerLineMatcher ownPlayerLineMatcher = new OwnPlayerLineMatcher();
        final OwnPlayer match = ownPlayerLineMatcher.match("[0003.45] DevOnline: Steam ID: 76561197962717112");
        assertNotNull(match);
        assertEquals("76561197962717112", match.getSteamId());
    }

}