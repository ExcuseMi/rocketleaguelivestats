package com.excuseme.rocketleaguelivestats.model;

public enum PlaylistType {
    DUEL(1, "Duel (Unranked)",false),
    DOUBLES(2, "Doubles (Unranked)", false),
    STANDARD(3, "Standard (Unranked)", false),
    CHAOS(4, "Chaos (Unranked)", false),

    DUEL_RANKED(10, "Solo Duel (Ranked)", true),
    DOUBLES_RANKED(11, "Doubles (Ranked)", true),
    SOLO_STANDARD_RANKED(12, "Solo Standard (Ranked)", true),
    STANDARD_RANKED(13, "Standard (Ranked)", true),

    UNKNOWN(0, "Unknown mode", false)
    ;


    private int playlistId;
    private String description;
    private boolean ranked;

    PlaylistType(int playlistId, String description, boolean ranked) {
        this.playlistId = playlistId;
        this.description = description;
        this.ranked = ranked;
    }

    public String getDescription() {
        return description;
    }

    public static PlaylistType findByPlaylistId(Integer playlistId) {
        if(playlistId == null) {
            return UNKNOWN;
        }
        for(PlaylistType playlistType : values()) {
            if(Integer.valueOf(playlistType.playlistId).equals(playlistId)) {
                return playlistType;
            }
        }
        return UNKNOWN;
    }

    public boolean isRanked() {
        return ranked;
    }
}
