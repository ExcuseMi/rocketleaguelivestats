package com.excuseme.rocketleaguelivestats.model;

public enum GamingSystem {
    STEAM("steam","gamingsystem/steam.png"), PS4("ps4","gamingsystem/Ps4.png"), BOT("other");

    private String qualifier;
    private String iconPath;

    private GamingSystem(String qualifier) {
        this.qualifier = qualifier;
        this.iconPath = null;
    }
    private GamingSystem(String qualifier, String iconPath) {
        this.qualifier = qualifier;
        this.iconPath = iconPath;
    }

    public String getQualifier() {
        return qualifier;
    }

    public String getIconPath() {
        return iconPath;
    }
}
