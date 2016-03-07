package com.excuseme.rocketleaguelivestats.scanner.matcher;

import com.excuseme.rocketleaguelivestats.scanner.model.NamePlate;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NamePlateDataLineMatcher implements LineMatcher<NamePlate> {
    private static final Pattern PATTERN = Pattern.compile("^\\[\\d*\\.?\\d*\\] Nameplate: RefreshNameplates NameplateData (.*) Row=(\\d*)$");

    public NamePlate match(String line) {
        final Matcher matcher = PATTERN.matcher(line);
        if(matcher.matches()) {
            return new NamePlate(matcher.group(1), Integer.parseInt(matcher.group(2).replace("'","")));
        }
        return null;
    }

}
