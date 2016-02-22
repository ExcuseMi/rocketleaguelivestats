package com.excuseme.rocketleaguelivestats.repository;

import com.excuseme.rocketleaguelivestats.scanner.model.GameData;

public interface GameDataListener {

    void gameDataChanged(GameData gameData);
}
