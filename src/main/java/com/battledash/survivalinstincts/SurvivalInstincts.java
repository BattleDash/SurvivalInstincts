package com.battledash.survivalinstincts;

import com.battledash.survivalinstincts.events.ActivationListeners;
import com.battledash.survivalinstincts.handlers.InstinctDataHandler;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SurvivalInstincts extends JavaPlugin {

    private static SurvivalInstincts INSTANCE;

    public static SurvivalInstincts getInstance() {
        return INSTANCE;
    }

    private InstinctDataHandler instinctDataHandler;

    @Override
    public void onEnable() {
        INSTANCE = this;
        instinctDataHandler = new InstinctDataHandler();
        this.registerEvents();
    }

    public InstinctDataHandler getInstinctDataHandler() {
        return instinctDataHandler;
    }

    public void registerEvents() {
        final PluginManager pluginManager = this.getServer().getPluginManager();
        pluginManager.registerEvents(new ActivationListeners(), this);
    }

}
