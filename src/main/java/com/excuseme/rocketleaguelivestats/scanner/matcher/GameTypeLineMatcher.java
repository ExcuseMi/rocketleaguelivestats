package com.excuseme.rocketleaguelivestats.scanner.matcher;

import com.excuseme.rocketleaguelivestats.scanner.model.GameType;

public class GameTypeLineMatcher implements LineMatcher<GameType> {

    private static final String DEV_ONLINE_SET_RICH_PRESENCE_TO = "DevOnline: Set rich presence to: ";
    private static final String IN = " in ";
    private static final String DATA_PLAYLIST = " data: Playlist-";

    //[0510.51] DevOnline: Set rich presence to: Doubles in Urban Central data: PlaylistType-11
    //[0090.50] DevOnline: Set rich presence to: Standard in Mannfield data: PlaylistType-3
    public GameType match(String line) {
        if(line.contains(DEV_ONLINE_SET_RICH_PRESENCE_TO) && line.contains(IN) && line.contains(DATA_PLAYLIST)) {
            final String description = line.substring(line.indexOf(DEV_ONLINE_SET_RICH_PRESENCE_TO) + DEV_ONLINE_SET_RICH_PRESENCE_TO.length(), line.indexOf(IN));
            final String mapName = line.substring(line.indexOf(IN) + IN.length(), line.indexOf(DATA_PLAYLIST));
            final Integer playlistType = Integer.valueOf(line.substring(line.indexOf(DATA_PLAYLIST)+DATA_PLAYLIST.length()));

            final GameType gameType = new GameType(mapName, playlistType, description);
            return gameType;
        }
        return null;
    }
}
