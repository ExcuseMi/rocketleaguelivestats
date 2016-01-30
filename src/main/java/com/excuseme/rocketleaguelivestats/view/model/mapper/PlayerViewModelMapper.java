package com.excuseme.rocketleaguelivestats.view.model.mapper;

import com.excuseme.rocketleaguelivestats.model.Player;
import com.excuseme.rocketleaguelivestats.view.model.PlayerViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Comparator;
import java.util.List;

public class PlayerViewModelMapper {

    public static ObservableList<PlayerViewModel> map(List<Player> playerList) {
        ObservableList<PlayerViewModel> playerViewModels = FXCollections.observableArrayList();
        for(Player player: playerList) {
            if(player != null) {
                playerViewModels.add(map(player));
            }
        }

        return playerViewModels;
    }
    public static PlayerViewModel map(Player player) {
        return new PlayerViewModel(player);
    }
}
