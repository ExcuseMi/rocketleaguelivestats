package com.excuseme.rocketleaguelivestats.scanner.matcher;

import com.excuseme.rocketleaguelivestats.scanner.model.PlayerName;

public class LoadoutValidationLineMatcher implements LineMatcher<PlayerName> {

    //[0092.43] GameEvent: PRI_TA_5 SetGameEvent PlayerName=bo-kw-saleen GameEvent=None InGameEvent=GameEvent_Soccar_TA_0
    public static final String PRI_TA_ = "PRI_TA_";
    public static final String GAME_EVENT = "GameEvent: "+ PRI_TA_;
    public static final String GAME_EVENT_NONE_IN_GAME_EVENT_GAME_EVENT_SOCCAR_TA_0 = "GameEvent=None InGameEvent=GameEvent_Soccar_TA_";
    public static final String SET_GAME_EVENT = "SetGameEvent";
    public static final String PLAYER_NAME = "PlayerName=";
    public static final String GAME_EVENT_NONE = "GameEvent=None";

    public PlayerName match(String line) {
        if(line.contains(GAME_EVENT) && line.contains(GAME_EVENT_NONE_IN_GAME_EVENT_GAME_EVENT_SOCCAR_TA_0)) {
            final int indexOf = line.indexOf(PRI_TA_);
            final int indexSetGameEvent = line.indexOf(SET_GAME_EVENT);
            final Integer number = Integer.valueOf(line.substring(indexOf + PRI_TA_.length(), indexSetGameEvent - 1));
            String name = line.substring(line.indexOf(PLAYER_NAME) + PLAYER_NAME.length(), line.indexOf(GAME_EVENT_NONE) -1);
            return new PlayerName(number, name);
        }
        return null;
    }
}
