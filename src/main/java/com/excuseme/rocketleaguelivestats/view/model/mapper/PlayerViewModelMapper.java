package com.excuseme.rocketleaguelivestats.view.model.mapper;

import com.excuseme.rocketleaguelivestats.model.Player;
import com.excuseme.rocketleaguelivestats.view.model.PlayerViewModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class PlayerViewModelMapper {

    public static void map(List<Player> playerList, ObservableList<PlayerViewModel> items) {
        for(Player player: playerList) {
            if(player != null) {
                final Optional<PlayerViewModel> first = items.stream().filter(p -> p.getPlayerIdentifier().equals(player.getPlayerIdentifier())).findFirst();
                if(first.isPresent()) {
                    update(first.get(), player);
                } else {
                    items.add(map(player));
                }
            }
        }
    }

    private static void update(PlayerViewModel playerViewModel, Player player) {
        playerViewModel.setActive(player.isActive());
    }

    public static PlayerViewModel map(Player player) {
        return new PlayerViewModel(player);
    }
}
