package com.excuseme.rocketleaguelivestats.scanner.matcher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AuthCodeMatcher implements LineMatcher<String> {
    private static final Pattern SEND_LOGIN_REQUEST = Pattern.compile("^\\[.*\\] Auth: SendLoginRequest PlatformAuthorizationCode=(.*)$");
    private static final Pattern HANDLE_RECEIVED_AUTH_CODE = Pattern.compile("^\\[.*\\] Auth: HandleReceivedAuthorizationCode bSuccess=True AuthorizationCode==(.*)$");

    @Override
    public String match(String line) {
        final Matcher matcher = SEND_LOGIN_REQUEST.matcher(line);
        if(matcher.matches()) {
            return matcher.group(1);
        }
        final Matcher matcher2 = HANDLE_RECEIVED_AUTH_CODE.matcher(line);
        if(matcher2.matches()) {
            return matcher2.group(1);
        }
        return null;
    }
}
