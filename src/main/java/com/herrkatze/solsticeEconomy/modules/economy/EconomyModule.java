package com.herrkatze.solsticeEconomy.modules.economy;

import com.herrkatze.solsticeEconomy.modules.economy.commands.BalanceCommand;
import com.herrkatze.solsticeEconomy.modules.economy.commands.EconomyCommand;
import com.herrkatze.solsticeEconomy.modules.economy.commands.PayCommand;
import com.herrkatze.solsticeEconomy.modules.economy.data.EconomyConfig;
import com.herrkatze.solsticeEconomy.modules.economy.data.EconomyLocale;
import com.herrkatze.solsticeEconomy.modules.economy.data.EconomyPlayerData;
import com.herrkatze.solsticeEconomy.modules.economy.data.EconomyServerData;
import com.herrkatze.solsticeEconomy.modules.economy.integration.computercraft.EconomyAPILoader;
import me.alexdevs.solstice.Solstice;
import me.alexdevs.solstice.api.module.ModuleBase;
import net.fabricmc.loader.api.FabricLoader;

import java.util.UUID;

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
        var hasCC = isCCPresent();
        if (hasCC){
            EconomyAPILoader.LoadEconomyAPI();
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
