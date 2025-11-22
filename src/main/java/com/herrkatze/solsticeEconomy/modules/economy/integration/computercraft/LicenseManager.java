package com.herrkatze.solsticeEconomy.modules.economy.integration.computercraft;

import com.herrkatze.solsticeEconomy.modules.economy.EconomyModule;
import com.snek.frameworklib.utils.Result;
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
        if (owner.is_ok()) {
            getModule().getPlayer(owner.unwrap()).key = null;
            getModule().getServerData().keyMap.remove(licenseKey);
        }else {
            Solstice.LOGGER.error("Attempt to invalidate license key for already invalid key.");
        }
    }

    private static EconomyModule getModule() {
        return Solstice.modules.getModule(EconomyModule.class);
    }
    public static Result<UUID,String> getOwner(UUID key){
        try {
            return Result.Ok(getModule().getServerData().keyMap.get(key));
        } catch (Exception e) {
            return Result.Err(e.toString());
        }
    }
    public static Result<UUID,String> getKey(UUID player){
        try {
            return Result.Ok(getModule().getPlayer(player).key);
        } catch (Exception e) {
            return Result.Err(e.toString());
        }
    }
}
