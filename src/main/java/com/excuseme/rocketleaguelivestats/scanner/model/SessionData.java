package com.excuseme.rocketleaguelivestats.scanner.model;

public class SessionData {
    private OwnPlayer ownPlayer;
    private String buildId;
    private String authCode;

    public OwnPlayer getOwnPlayer() {
        return ownPlayer;
    }

    public void setOwnPlayer(OwnPlayer ownPlayer) {
        this.ownPlayer = ownPlayer;
    }

    public String getBuildId() {
        return buildId;
    }

    public void setBuildId(String buildId) {
        this.buildId = buildId;
    }

    public String getAuthCode() {
        return authCode;
    }

    public void setAuthCode(String authCode) {
        this.authCode = authCode;
    }
}
