package com.herrkatze.solsticeEconomy.modules.economy;

import com.herrkatze.solsticeEconomy.modules.economy.commands.BalanceCommand;
import com.herrkatze.solsticeEconomy.modules.economy.commands.EconomyCommand;
import com.herrkatze.solsticeEconomy.modules.economy.commands.PayCommand;
import com.herrkatze.solsticeEconomy.modules.economy.data.EconomyConfig;
import com.herrkatze.solsticeEconomy.modules.economy.data.EconomyLocale;
import com.herrkatze.solsticeEconomy.modules.economy.data.EconomyPlayerData;
import com.herrkatze.solsticeEconomy.modules.economy.data.EconomyServerData;
import com.herrkatze.solsticeEconomy.modules.economy.integration.computercraft.CCEvents;
import com.herrkatze.solsticeEconomy.modules.economy.integration.computercraft.EconomyAPILoader;
import me.alexdevs.solstice.Solstice;
import me.alexdevs.solstice.api.events.SolsticeEvents;
import me.alexdevs.solstice.api.module.ModuleBase;
import me.alexdevs.solstice.modules.afk.AfkModule;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;
import java.util.concurrent.TimeUnit;


public class EconomyModule extends ModuleBase.Toggleable {
    public static final String ID = "economy";

    public EconomyModule() {
        super(ID);
    }

    @Override
    public void init() {
        Solstice.configManager.registerData(ID, EconomyConfig.class, EconomyConfig::new);
        Solstice.localeManager.registerModule(ID, EconomyLocale.MODULE);
        Solstice.playerData.registerData(ID, EconomyPlayerData.class, EconomyPlayerData::new);
        Solstice.serverData.registerData(ID, EconomyServerData.class,EconomyServerData::new);

        commands.add(new EconomyCommand(this));
        commands.add(new BalanceCommand(this));
        commands.add(new PayCommand(this));
        SolsticeEvents.READY.register((instance, server)-> {
            if(getConfig().autoPayInterval != 0 && getConfig().autoPayAmount != 0) { // if either config value is set to 0, disable the feature entirely. no need for a separate toggle
                Solstice.scheduler.scheduleAtFixedRate(this::calculatePayment, 0, getConfig().autoPayInterval, TimeUnit.SECONDS);
            }
        });
        var hasCC = isCCPresent();
        if (hasCC){
            EconomyAPILoader.LoadEconomyAPI();
        }

    }

    private void calculatePayment(){
        var server = Solstice.server;
        var players = server.getPlayerList().getPlayers();
        var config = getConfig();
        var interval = config.autoPayInterval;
        var amount = config.autoPayAmount;
        for (ServerPlayer player:players) {
            var playerData = getPlayer(player.getUUID());
            var activeTime = Solstice.modules.getModule(AfkModule.class).getActiveTime(player.getUUID());
            var oldActiveTime = playerData.oldActiveTime;
            double deltaActiveTime = activeTime - oldActiveTime; // double can hold a 32 bit int just fine, it's needed to get accurate data
            double relativeDeltaActiveTime = deltaActiveTime / interval; // make it 1 if equal to interval, or greater if they log off and rejoin mid-interval
            long balance = (long) (amount * relativeDeltaActiveTime);
            if(EconomyModule.isCCPresent()) {
                CCEvents.fireEvent(player.getUUID(),"balance_change",(double) playerData.balance / 100d,(double) balance/100d,CurrencyRenderer.renderCurrency(playerData.balance).getString(),CurrencyRenderer.renderCurrency(balance).getString());
            }
            EconomyManager.addCurrency(player.getUUID(),balance);
            NotificationManager.sendNotification(PlayerBalanceNotifications.EarningNotification(balance),player);
            playerData.oldActiveTime = activeTime;
        }
    }
    public static boolean isCCPresent() {
        var optContainer = FabricLoader.getInstance().getModContainer("computercraft");
        return optContainer.isPresent();
    }

    public static EconomyConfig getConfig() {
        return Solstice.configManager.getData(EconomyConfig.class);
    }

    public EconomyPlayerData getPlayer(UUID uuid) {
        return Solstice.playerData.get(uuid).getData(EconomyPlayerData.class);
    }
    public EconomyServerData getServerData() {
        return Solstice.serverData.getData(EconomyServerData.class);
    }

}
