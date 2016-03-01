package com.excuseme.rocketleaguelivestats.scanner;

import com.excuseme.rocketleaguelivestats.repository.GameDataListener;
import com.excuseme.rocketleaguelivestats.scanner.matcher.*;
import com.excuseme.rocketleaguelivestats.scanner.model.*;
import com.excuseme.rocketleaguelivestats.scanner.tailer.Tailer;
import com.excuseme.rocketleaguelivestats.scanner.tailer.TailerListenerAdapter;

import java.io.File;
import java.util.*;

public class TailingFileScanner {
    private List<LineMatcher<?>> matchers = Arrays.asList(new EventLineMatcher(), new LoadoutValidationLineMatcher(),
            new NamePlateDataLineMatcher(), new RegisterPlayerWithSessionLineMatcher(), new HandlePlayerRemovedLineMatcher(), new GameTypeLineMatcher());

    private OwnPlayerLineMatcher ownPlayerLineMatcher;
    private BuildIdLineMatcher buildIdLineMatcher;
    private AuthCodeMatcher authCodeMatcher;
    private GameDataListener gameDataListener;

    public TailingFileScanner(GameDataListener gameDataListener, File file) {
        this.gameDataListener = gameDataListener;
        final MyTailerListener myTailerListener = new MyTailerListener();
        Tailer tailer = new Tailer(file, myTailerListener, 1000, false, true, 4096);
        Thread thread = new Thread(tailer);
        thread.setDaemon(true);
        thread.start();
        ownPlayerLineMatcher = new OwnPlayerLineMatcher();
        buildIdLineMatcher = new BuildIdLineMatcher();
        authCodeMatcher = new AuthCodeMatcher();
    }

    public class MyTailerListener extends TailerListenerAdapter {
        private GameData gameData = null;
        private GameData recentValidData = null;
        private SessionData sessionData = new SessionData();

        @Override
        public void fileRotated() {
            if(recentValidData != null && !recentValidData.isEmpty()) {
                sessionData = new SessionData();
                gameDataListener.gameDataChanged(recentValidData);
            }
        }

        @Override
        public void endOfFile() {
            if(recentValidData != null && !recentValidData.isEmpty()) {
                gameDataListener.sessionDataChanged(sessionData);
                gameDataListener.gameDataChanged(recentValidData);
            }
        }

        public void handle(String line) {
            if(sessionData.getBuildId() == null) {
                final String match = buildIdLineMatcher.match(line);
                if(match != null) {
                    sessionData.setBuildId(match);
                }
            }
            if (sessionData.getOwnPlayer() == null) {
                final OwnPlayer match = ownPlayerLineMatcher.match(line);
                if (match != null) {
                    sessionData.setOwnPlayer(match);
                }
            }
            final String match = authCodeMatcher.match(line);
            if(match != null) {
                sessionData.setAuthCode(match);
            }
            GameData newGameData = scanLine(line, gameData != null ? gameData.clone() : null);
            if(newGameData != null) {
                gameData = newGameData;
                if(!gameData.isEmpty()) {
                    recentValidData = gameData;
                    recentValidData.setOwnPlayer(sessionData.getOwnPlayer());
                }
            }

        }
    }

    private GameData scanLine(String line, GameData gameData) {
        for (LineMatcher matcher : matchers) {
            Object match = matcher.match(line);
            if (match != null) {
                if (Event.class.isInstance(match)) {
                    final Event event = Event.class.cast(match);
                    if (Event.EventType.GAME_LOADED.equals(event.getEventType())) {
                        return new GameData(event.getIdentifier());

                    } else if (Event.EventType.GAME_ENDED.equals(event.getEventType())) {
                        gameData.setEnded(true);
                        return gameData;

                    }
                } else if (NamePlate.class.isInstance(match)) {
                    gameData.addNamePlate(NamePlate.class.cast(match));
                    return gameData;
                } else if (PlayerName.class.isInstance(match)) {
                    gameData.addPlayerName(PlayerName.class.cast(match));
                    return gameData;
                } else if (PlayerId.class.isInstance(match)) {
                    gameData.addPlayerId(PlayerId.class.cast(match));
                    return gameData;
                } else if (HandlePlayerRemoved.class.isInstance(match)) {
                    gameData.addPlayerRemoved(HandlePlayerRemoved.class.cast(match));
                    return gameData;
                } else if (GameType.class.isInstance(match)) {
                    gameData.setGameType(GameType.class.cast(match));
                    return gameData;
                }
            }
        }
        return null;
    }
}
