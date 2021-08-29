package com.battledash.survivalinstincts.handlers;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class InstinctDataHandler {

    private final Map<Player, InstinctData> playerInstinctData = new HashMap<>();

    public InstinctData createInstinctData(Player player) {
        final InstinctData data = new InstinctData();
        playerInstinctData.put(player, data);
        return data;
    }

    public InstinctData removeInstinctData(Player player) {
        return playerInstinctData.remove(player);
    }

    public InstinctData getInstinctData(Player player) {
        return playerInstinctData.containsKey(player) ? playerInstinctData.get(player) : createInstinctData(player);
    }

}
