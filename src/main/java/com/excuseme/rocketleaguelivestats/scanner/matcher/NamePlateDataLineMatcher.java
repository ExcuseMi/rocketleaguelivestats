package com.excuseme.rocketleaguelivestats.scanner.matcher;

import com.excuseme.rocketleaguelivestats.scanner.model.NamePlate;

public class NamePlateDataLineMatcher implements LineMatcher<NamePlate> {
    private static final String NAMEPLATE_REFRESH_NAMEPLATES_NAMEPLATE_DATA = "Nameplate: RefreshNameplates NameplateData ";
    private static final String ROW = " Row=";

    public NamePlate match(String line) {
        if(line.contains(NAMEPLATE_REFRESH_NAMEPLATES_NAMEPLATE_DATA) && line.contains(ROW)) {
            String substring = line.substring(line.indexOf(NAMEPLATE_REFRESH_NAMEPLATES_NAMEPLATE_DATA) + NAMEPLATE_REFRESH_NAMEPLATES_NAMEPLATE_DATA.length());
            String[] split = substring.split(ROW);
            return new NamePlate(split[0], Integer.parseInt(split[1].replace("'","")));
        }
        return null;
    }

}
