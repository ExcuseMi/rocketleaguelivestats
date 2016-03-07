package com.excuseme.rocketleaguelivestats.scanner.matcher;

import com.excuseme.rocketleaguelivestats.scanner.model.PlayerName;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoadoutValidationLineMatcher implements LineMatcher<PlayerName> {

    //[0092.43] GameEvent: PRI_TA_5 SetGameEvent PlayerName=bo-kw-saleen GameEvent=None InGameEvent=GameEvent_Soccar_TA_0
    private static final Pattern PATTERN = Pattern.compile("^\\[\\d*\\.?\\d*\\] GameEvent: PRI_TA_(\\d*) SetGameEvent PlayerName=(.*) GameEvent=None InGameEvent=GameEvent_Soccar_TA_(\\d*)$");

    public PlayerName match(String line) {
        final Matcher matcher = PATTERN.matcher(line);
        if(matcher.matches()) {
            final Integer number = Integer.valueOf(matcher.group(1));
            String name = matcher.group(2);
            return new PlayerName(number, name);
        }
        return null;
    }
}
