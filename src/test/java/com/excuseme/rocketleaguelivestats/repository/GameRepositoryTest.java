package com.excuseme.rocketleaguelivestats.repository;

import com.excuseme.rocketleaguelivestats.model.Game;
import org.junit.Test;

import java.net.URL;
import java.util.List;

import static org.junit.Assert.assertNotNull;

public class GameRepositoryTest {

    @Test
    public void test() throws NoLogException {
        URL url = Thread.currentThread().getContextClassLoader().getResource("log1.txt");

        GameRepository gameRepository = new GameRepository(url.getPath());
        Game latestGame = gameRepository.findLatestGame();
        assertNotNull(latestGame);
    }

    @Test
    public void testGamesInSession() throws NoLogException {
        URL url = Thread.currentThread().getContextClassLoader().getResource("log1.txt");

        GameRepository gameRepository = new GameRepository(url.getPath());
        List<Game> gamesInSession = gameRepository.findGamesInSession();
        assertNotNull(gamesInSession);
    }
}