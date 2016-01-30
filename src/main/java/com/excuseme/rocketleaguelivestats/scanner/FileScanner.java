package com.excuseme.rocketleaguelivestats.scanner;

import com.excuseme.rocketleaguelivestats.scanner.matcher.*;
import com.excuseme.rocketleaguelivestats.scanner.model.*;

import java.io.File;
import java.util.*;

public class FileScanner {
    private final File file;
    private List<LineMatcher<?>> matchers = Arrays.asList(new EventLineMatcher(), new LoadoutValidationLineMatcher(),
            new NamePlateDataLineMatcher(), new RegisterPlayerWithSessionLineMatcher(), new HandlePlayerRemovedLineMatcher(), new GameTypeLineMatcher());

    private OwnPlayerLineMatcher ownPlayerLineMatcher;
    public FileScanner(File file) {
        this.file = file;
        ownPlayerLineMatcher = new OwnPlayerLineMatcher();
    }

    public List<GameData> scan() {
        final List<GameData> gameDatas = new ArrayList<GameData>();
        GameData gameData = null;
        final OwnPlayer ownPlayer = findOwnPlayer();
        Scanner input = null;
        try {
            input = new Scanner(file);
            while (input.hasNextLine()) {
                String line = input.nextLine();
                gameData = scanLine(line, gameData);
                if(gameData != null  && !gameDatas.contains(gameData)) {
                    gameData.setOwnPlayer(ownPlayer);
                    gameDatas.add(gameData);
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();

        }finally {
            if(input != null) {
                input.close();
            }
        }
        final List<GameData> filterGames = new ArrayList<GameData>();
        for(GameData gameData1 : gameDatas) {
            if(!gameData1.isEmpty()) {
                filterGames.add(gameData1);
            }
        }
        return filterGames;
    }

    public OwnPlayer findOwnPlayer() {
        Scanner input = null;
        try {
            input = new Scanner(file);
            while (input.hasNextLine()) {
                String line = input.nextLine();
                final OwnPlayer match = ownPlayerLineMatcher.match(line);
                if(match != null){
                    return match;
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            input.close();
        }
        return null;
    }

    private GameData scanLine(String line, GameData gameData) {
        GameData newGameData = gameData;
        for(LineMatcher matcher : matchers) {
            Object match = matcher.match(line);
            if(match != null) {
                if(Event.class.isInstance(match)) {
                    final Event event = Event.class.cast(match);
                    if(Event.EventType.GAME_LOADED.equals(event.getEventType())) {
                        newGameData = new GameData(event.getIdentifier());
                    } else if(Event.EventType.GAME_ENDED.equals(event.getEventType())) {
                        newGameData.setEnded(true);
                    }
                }else if(NamePlate.class.isInstance(match)) {
                    newGameData.addNamePlate(NamePlate.class.cast(match));
                } else if(PlayerName.class.isInstance(match)) {
                    newGameData.addPlayerName(PlayerName.class.cast(match));
                } else if(PlayerId.class.isInstance(match)) {
                    newGameData.addPlayerId(PlayerId.class.cast(match));
                } else if(HandlePlayerRemoved.class.isInstance(match)) {
                    newGameData.addPlayerRemoved(HandlePlayerRemoved.class.cast(match));
                } else if(GameType.class.isInstance(match)) {
                    newGameData.setGameType(GameType.class.cast(match));
                }
            }
        }
        return newGameData;
    }
}
