package com.battledash.survivalinstincts.events;

import com.battledash.survivalinstincts.handlers.InstinctInstance;
import com.battledash.survivalinstincts.SurvivalInstincts;
import com.battledash.survivalinstincts.instincts.WaypointInstinct;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ActivationListeners implements Listener {

    @EventHandler
    public void dropEvent(PlayerDropItemEvent event) {
        event.setCancelled(true);
        SurvivalInstincts.getInstance().getInstinctDataHandler().getInstinctData(event.getPlayer()).getInstincts().add(new WaypointInstinct(event.getPlayer().getLocation()));
        new InstinctInstance(event.getPlayer()).start();
    }

}
