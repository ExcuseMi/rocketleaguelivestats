package com.excuseme.rocketleaguelivestats.model;

public enum GamingSystem {
    STEAM("steam"), PS4("psn"), BOT("other");

    private String qualifier;

    private GamingSystem(String qualifier) {
        this.qualifier = qualifier;
    }

    public String getQualifier() {
        return qualifier;
    }
}
