package com.excuseme.rocketleaguelivestats.scanner.matcher;

import com.excuseme.rocketleaguelivestats.scanner.model.GameType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GameTypeLineMatcher implements LineMatcher<GameType> {

    private static final Pattern DEV_ONLINE_SET_RICH_PRESENCE_TO = Pattern.compile("^\\[\\d*\\.?\\d*\\] DevOnline: Set rich presence to: (.*) in (.*) data: Playlist-(\\d+)$");

    //[0510.51] DevOnline: Set rich presence to: Doubles in Urban Central data: Playlist-11
    //[0090.50] DevOnline: Set rich presence to: Standard in Mannfield data: Playlist-3
    public GameType match(String line) {
        final Matcher matcher = DEV_ONLINE_SET_RICH_PRESENCE_TO.matcher(line);
        if(matcher.matches()) {
            final String description = matcher.group(1);
            final String mapName = matcher.group(2);
            final Integer playlistType = Integer.valueOf(matcher.group(3));

            return new GameType(mapName, playlistType, description);
        }
        return null;
    }
}
