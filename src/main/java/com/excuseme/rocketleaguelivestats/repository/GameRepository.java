package com.excuseme.rocketleaguelivestats.repository;

import com.excuseme.rocketleaguelivestats.model.Game;
import com.excuseme.rocketleaguelivestats.scanner.FileScanner;
import com.excuseme.rocketleaguelivestats.scanner.model.GameData;
import javafx.scene.control.Alert;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.util.List;

public class GameRepository {
    public static final String FOLDER =FileSystemView.getFileSystemView().getDefaultDirectory().getPath() + File.separator + "my games" + File.separator + "Rocket League" + File.separator + "TAGame" + File.separator + "Logs" + File.separator;
    private static final String LAUNCH = "Launch.log";
    private final String path;

    public static GameRepository createDefault() {
        String path = FOLDER + LAUNCH;
        return new GameRepository(path);
    }

    GameRepository(String path) {
        this.path = path;
    }

    public Game findLatestGame() throws NoLogException{
        File file = new File(path);
        if(file.exists()) {
            FileScanner fileScanner = new FileScanner(file);
            List<GameData> gameDatas = fileScanner.scan();
            if (gameDatas.isEmpty()) {
                return null;
            }
            GameData gameData = gameDatas.get(gameDatas.size() - 1);
            final Game game = GameMapper.map(gameData);

            return game;
        } else {
            throw new NoLogException("No log file found in " + path);
        }
    }

    public List<Game> findGamesInSession() throws NoLogException{
        File file = new File(path);
        if(file.exists()) {
        FileScanner fileScanner = new FileScanner(file);
        List<GameData> gameDatas = fileScanner.scan();
        return GameMapper.map(gameDatas);
        } else {
            throw new NoLogException("No log file found in " + path);
        }
    }
}
