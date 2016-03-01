package com.excuseme.rocketleaguelivestats.repository;

import com.excuseme.rocketleaguelivestats.scanner.model.GameData;
import com.excuseme.rocketleaguelivestats.scanner.model.SessionData;

public interface GameDataListener {
    void sessionDataChanged(SessionData sessionData);
    void gameDataChanged(GameData gameData);
}
