package com.excuseme.rocketleaguelivestats.scanner.matcher;

import com.excuseme.rocketleaguelivestats.scanner.model.GameType;
import org.junit.Test;

import static org.junit.Assert.*;

public class GameTypeLineMatcherTest {

    public static final String DOUBLES_RANKED = "[0510.51] DevOnline: Set rich presence to: Doubles in Urban Central data: Playlist-11";
    public static final String STANDARD_RANKED = "[0090.50] DevOnline: Set rich presence to: Standard in Mannfield data: Playlist-13";
    private static final String MAIN_MENU = "[2441.68] DevOnline: Set rich presence to: Main Menu data: Menu";
    private static final String UNRANKED_STANDARD = "[0827.94] DevOnline: Set rich presence to: Standard in Beckwith Park data: Playlist-3";
    public static final String DEV_ONLINE_SET_RICH_PRESENCE_TO_SNOW_DAY_IN_DFH_STADIUM_SNOWY_DATA_PLAYLIST_15 = "[0097.30] DevOnline: Set rich presence to: Snow Day in DFH Stadium (Snowy) data: Playlist-15";

    @Test
    public void testMatch() throws Exception {
        final GameTypeLineMatcher gameTypeLineMatcher = new GameTypeLineMatcher();
        GameType match = gameTypeLineMatcher.match(DOUBLES_RANKED);
        assertNotNull(match);

        assertEquals("Doubles",match.getGameDescription());
        assertEquals("Urban Central",match.getMapName());
        assertEquals(Integer.valueOf(11),match.getPlaylistType());

        match = gameTypeLineMatcher.match(STANDARD_RANKED);
        assertNotNull(match);

        assertEquals("Standard",match.getGameDescription());
        assertEquals("Mannfield",match.getMapName());
        assertEquals(Integer.valueOf(13),match.getPlaylistType());

        match = gameTypeLineMatcher.match(MAIN_MENU);
        assertNull(match);

        match = gameTypeLineMatcher.match(UNRANKED_STANDARD);
        assertNotNull(match);

        assertEquals("Standard",match.getGameDescription());
        assertEquals("Beckwith Park",match.getMapName());
        assertEquals(Integer.valueOf(3),match.getPlaylistType());

        match = gameTypeLineMatcher.match(DEV_ONLINE_SET_RICH_PRESENCE_TO_SNOW_DAY_IN_DFH_STADIUM_SNOWY_DATA_PLAYLIST_15);
        assertNotNull(match);

        assertEquals("Snow Day",match.getGameDescription());
        assertEquals("DFH Stadium (Snowy)",match.getMapName());
        assertEquals(Integer.valueOf(15),match.getPlaylistType());

    }
}