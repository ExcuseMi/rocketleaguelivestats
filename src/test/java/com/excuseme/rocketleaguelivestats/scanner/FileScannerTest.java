package com.excuseme.rocketleaguelivestats.scanner;

import com.excuseme.rocketleaguelivestats.scanner.model.GameData;
import org.junit.Test;

import java.io.File;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.*;

public class FileScannerTest {

    @Test
    public void testLog1() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("log1.txt");
        File file = new File(url.getPath());
        FileScanner fileScanner = new FileScanner(file);
        List<GameData> gameDatas = fileScanner.scan();

        assertNotNull(gameDatas);
    }

    @Test
    public void testLog2() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("log2.txt");
        File file = new File(url.getPath());
        FileScanner fileScanner = new FileScanner(file);
        List<GameData> gameDatas = fileScanner.scan();

        assertNotNull(gameDatas);

    }

    @Test
    public void test112() {
        URL url = Thread.currentThread().getContextClassLoader().getResource("Launch-1.12.log");
        File file = new File(url.getPath());
        FileScanner fileScanner = new FileScanner(file);
        List<GameData> gameDatas = fileScanner.scan();

        assertNotNull(gameDatas);

    }

}