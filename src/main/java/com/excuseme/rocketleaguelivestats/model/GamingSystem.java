package com.excuseme.rocketleaguelivestats.model;

public enum GamingSystem {
    STEAM("steam","gamingsystem/steam.png", 3), PS4("ps4","gamingsystem/Ps4.png",2), XBOX("xbox","",1), BOT("other", "", null);

    private String qualifier;
    private String iconPath;
    private Integer apiId;

    private GamingSystem(String qualifier, String iconPath, Integer apiId) {
        this.qualifier = qualifier;
        this.iconPath = iconPath;
        this.apiId = apiId;

    }

    public String getQualifier() {
        return qualifier;
    }

    public String getIconPath() {
        return iconPath;
    }

    public Integer getApiId() { return apiId; }
}
