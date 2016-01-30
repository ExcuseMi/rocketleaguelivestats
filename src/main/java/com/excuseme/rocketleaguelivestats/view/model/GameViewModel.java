package com.excuseme.rocketleaguelivestats.view.model;

import com.excuseme.rocketleaguelivestats.model.Game;
import com.excuseme.rocketleaguelivestats.model.PlaylistType;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;

public class GameViewModel {
    private Game game;
    private SimpleStringProperty labelProperty;

    public GameViewModel() {
        this.labelProperty = new SimpleStringProperty();
    }

    public String getLabelProperty() {
        return labelProperty.get();
    }

    public SimpleStringProperty labelPropertyProperty() {
        return labelProperty;
    }

    public void setLabelProperty(String labelProperty) {
        this.labelProperty.set(labelProperty);
    }

    public boolean isSameGame(Game game) {
        if(this.game == null) {
            return false;
        }
        return this.game.equals(game);
    }

    public String getGameIdentifier() {
        return game != null ? game.getIdentifier() : null;
    }

    public void updateGame(Game latestGame) {
        this.game = latestGame;
        Platform.runLater(new Runnable() {
            public void run() {
                setLabelProperty(game != null && game.getPlaylist() != null ? game.getPlaylist().getPlaylistDescription() + " on " + game.getMap() +
                        (game.getPlaylist().getPlaylistType().isRanked() ? " [RANKED]" : " [UNRANKED]") :  "No games in the log");
            }
        });
    }


    public Integer calculateMostImportantColumn() {
        if(game!=null && game.getPlaylist() != null) {
            final PlaylistType playlistType = game.getPlaylist().getPlaylistType();
            if(PlaylistType.DUEL_RANKED.equals(playlistType)) {
                return 2;
            } else if(PlaylistType.DOUBLES_RANKED.equals(playlistType)) {
                return 3;
            } else if(PlaylistType.SOLO_STANDARD_RANKED.equals(playlistType)) {
                return 4;
            } else if(PlaylistType.STANDARD_RANKED.equals(playlistType)) {
                return 5;
            }
        }
        return null;
    }
}
