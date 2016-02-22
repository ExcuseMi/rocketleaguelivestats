package com.excuseme.rocketleaguelivestats.scanner.matcher;

import com.excuseme.rocketleaguelivestats.scanner.model.PlayerId;

public class RegisterPlayerWithSessionLineMatcher implements LineMatcher<PlayerId> {
//[1945.61] ScriptLog: PRI_TA_18 RegisterPlayerWithSession Steam|76561197962717112|0 Game

    public static final String REGISTER_PLAYER_WITH_SESSION = "RegisterPlayerWithSession";
    public static final String PRI_TA_ = "PRI_TA_";

    public PlayerId match(String line) {
        if(line.contains(REGISTER_PLAYER_WITH_SESSION)) {
            int beginIndex = line.indexOf(PRI_TA_) + PRI_TA_.length();
            String numberAsText = line.substring(beginIndex, line.indexOf(REGISTER_PLAYER_WITH_SESSION)-1);
            Integer number = Integer.valueOf(numberAsText);
            int startIndex = line.indexOf(REGISTER_PLAYER_WITH_SESSION) + REGISTER_PLAYER_WITH_SESSION.length() + 1;
            String substring = line.substring(startIndex);
            int spaceIndex = substring.indexOf(" ");
            String identifier = substring.substring(0, spaceIndex - 1);
            String[] split = identifier.split("\\|",2);
            return new PlayerId(number, split[1].substring(0,split[1].lastIndexOf("|")), split[0]);
        }
        return null;
    }
}
