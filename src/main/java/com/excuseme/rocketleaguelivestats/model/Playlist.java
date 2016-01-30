package com.excuseme.rocketleaguelivestats.model;

public class Playlist {
    private String playlistDescription;
    private PlaylistType playlistType;

    public Playlist(String playlistDescription, PlaylistType playlistType) {
        this.playlistDescription = playlistDescription;
        this.playlistType = playlistType;
    }

    public String getPlaylistDescription() {
        return playlistDescription;
    }

    public PlaylistType getPlaylistType() {
        return playlistType;
    }

    @Override
    public String toString() {
        return "Playlist{" +
                "playlistDescription='" + playlistDescription + '\'' +
                ", playlistType=" + playlistType +
                '}';
    }
}
