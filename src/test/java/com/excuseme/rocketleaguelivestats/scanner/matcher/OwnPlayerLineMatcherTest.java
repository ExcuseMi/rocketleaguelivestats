package com.excuseme.rocketleaguelivestats.scanner.matcher;

import com.excuseme.rocketleaguelivestats.scanner.model.OwnPlayer;
import org.junit.Test;

import static org.junit.Assert.*;

public class OwnPlayerLineMatcherTest {

    @Test
    public void test() {
        final OwnPlayerLineMatcher ownPlayerLineMatcher = new OwnPlayerLineMatcher();
        final OwnPlayer match = ownPlayerLineMatcher.match("[0003.45] DevOnline: Steam ID: 212312312");
        assertNotNull(match);
        assertEquals("212312312", match.getSteamId());
    }

}