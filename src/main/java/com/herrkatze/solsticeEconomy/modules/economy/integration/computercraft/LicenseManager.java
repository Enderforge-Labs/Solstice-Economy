package com.herrkatze.solsticeEconomy.modules.economy.integration.computercraft;

import com.herrkatze.solsticeEconomy.modules.economy.EconomyModule;
import me.alexdevs.solstice.Solstice;

import java.util.UUID;

public class LicenseManager {
    public static UUID createLicense(UUID owner){
        var key = UUID.randomUUID();
        var module = getModule();
        var serverData = module.getServerData();
        var playerData = module.getPlayer(owner);
        if (playerData.key != null) {
            invalidateKey(playerData.key);
        }
        playerData.key = key;
        serverData.keyMap.put(key,owner);
        return key;
    }

    public static void invalidateKey(UUID licenseKey) {
        CCEvents.removeAllComputers(licenseKey);
        var owner = getOwner(licenseKey);
        getModule().getPlayer(owner).key = null;
        getModule().getServerData().keyMap.remove(licenseKey);
    }

    private static EconomyModule getModule() {
        return Solstice.modules.getModule(EconomyModule.class);
    }
    public static UUID getOwner(UUID key){
        return getModule().getServerData().keyMap.get(key);
    }
    public static UUID getKey(UUID player){
        return getModule().getPlayer(player).key;
    }
}
