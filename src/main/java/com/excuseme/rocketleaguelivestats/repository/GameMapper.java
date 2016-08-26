package com.excuseme.rocketleaguelivestats.repository;

import com.excuseme.rocketleaguelivestats.model.*;
import com.excuseme.rocketleaguelivestats.scanner.model.OwnPlayer;
import com.excuseme.rocketleaguelivestats.scanner.model.*;

import java.util.*;

public class GameMapper {

    public static final String SUFFIX = "(2)";

    public static List<Game> map(List<GameData> gameDatas) {
        List<Game> games = new ArrayList<Game>();
        for (GameData gameData : gameDatas) {
            Game game = map(gameData);
            games.add(game);
        }
        return games;
    }

    public static Game map(GameData gameData) {
        if (gameData == null) {
            return null;
        }
        String identifier = gameData.getIdentifier();
        Set<PlayerId> playerIds = gameData.getPlayerIds();
        Set<PlayerName> playerNames = gameData.getPlayerNames();
        Set<HandlePlayerRemoved> playerRemoveds = gameData.getPlayerRemoveds();
        final GameType gameType = gameData.getGameType();
        final List<Player> players = new ArrayList<Player>();

        for (PlayerName playerName : playerNames) {

            PlayerId playerId = findPlayerId(playerIds, playerName.getNumber());
            if (playerId == null) {
                final PlayerName extensionPlayer = findExtensionPlayer(playerNames, playerName.getName());
                if (extensionPlayer != null) {
                    playerId = findPlayerId(playerIds, extensionPlayer.getNumber());
                }
            }
            String system = playerId != null ? playerId.getSystem() : null;
            GamingSystem gamingSystem = mapSystem(system);

            int number = playerId != null ? playerId.getNumber() : -1;

            String id = GamingSystem.XBOX.equals(gamingSystem) ? playerName.getName() : playerId != null ? playerId.getId() : null;

            boolean ownPlayer = calculateOwnPlayer(playerId, gameData.getOwnPlayer(), gamingSystem);
            String nickName = playerName.getName();
            int row = 0;
            boolean active = isPlayerActive(playerRemoveds, playerName);
            final Player player = new Player(number, row, nickName, new PlayerIdentifier(id, gamingSystem), active, ownPlayer);
            players.add(player);
        }

        Collections.sort(players, new Comparator<Player>() {
            public int compare(Player o1, Player o2) {
                return Integer.compare(o1.getRow(), o2.getRow());
            }

        });
        final Integer playlistTypeInt = gameType != null ? gameType.getPlaylistType() : null;
        final String mapName = gameType != null ?  gameType.getMapName() : null;
        final PlaylistType playlistType = PlaylistType.findByPlaylistId(playlistTypeInt);
        final Playlist playlist = new Playlist(gameType != null ? gameType.getGameDescription() : "Unknown", playlistType);
        return new Game(identifier, players, mapName, playlist);
    }

    private static boolean calculateOwnPlayer(PlayerId playerId, OwnPlayer ownPlayer, GamingSystem gamingSystem) {
        return ownPlayer != null && GamingSystem.STEAM.equals(gamingSystem) && ownPlayer.getSteamId().equals(playerId.getId());
    }

    private static boolean isPlayerActive(Set<HandlePlayerRemoved> playerRemoveds, PlayerName playerName) {
        if(playerRemoveds != null) {
            for (HandlePlayerRemoved handlePlayerRemoved : playerRemoveds) {
                if (handlePlayerRemoved.getName().equals(playerName.getName())) {
                    return false;
                }
            }
        }
        return true;
    }

    private static PlayerName findExtensionPlayer(Set<PlayerName> playerNames, String name) {
        String nameToSearch = name;
        if (name.endsWith(SUFFIX)) {
            nameToSearch = name.substring(0, name.length() - SUFFIX.length());
        }
        for (PlayerName playerName : playerNames) {
            String currentName = playerName.getName();
            if (currentName.endsWith(SUFFIX)) {
                currentName = playerName.getName().substring(0, currentName.length() - SUFFIX.length());
            }
            if (name.endsWith(SUFFIX) || currentName.endsWith(SUFFIX)) {
                if (nameToSearch.equals(currentName)) {
                    return playerName;
                }
            }
        }
        return null;
    }

    private static GamingSystem mapSystem(String system) {
        if ("STEAM".equalsIgnoreCase(system)) {
            return GamingSystem.STEAM;
        } else if ("PS4".equalsIgnoreCase(system)) {
            return GamingSystem.PS4;
        } else if ("XboxOne".equalsIgnoreCase(system)) {
            return GamingSystem.XBOX;
        } else {
            return GamingSystem.BOT;
        }

    }

    private static PlayerId findPlayerId(Set<PlayerId> playerIds, int number) {
        for (PlayerId playerId : playerIds) {
            if (number == playerId.getNumber()) {
                return playerId;
            }
        }
        return null;
    }
}
