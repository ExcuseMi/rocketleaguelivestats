package com.excuseme.rocketleaguelivestats.repository;

import com.excuseme.rocketleaguelivestats.scanner.model.GameData;
import com.excuseme.rocketleaguelivestats.scanner.model.SessionData;

public interface GameDataListener {
    void gameDataChanged(GameData gameData);
}
