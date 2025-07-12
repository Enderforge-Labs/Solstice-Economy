package com.herrkatze.solsticeEconomy.modules.economy;

import com.herrkatze.solsticeEconomy.SolsticeEconomy;
import com.herrkatze.solsticeEconomy.modules.economy.data.EconomyPlayerData;
import com.herrkatze.solsticeEconomy.modules.economy.data.EconomyServerData;
import com.herrkatze.solsticeEconomy.modules.economy.integration.computercraft.CCEvents;
import me.alexdevs.solstice.Solstice;

import java.util.UUID;

// Checks here in the manager are meant as sanity checks on the inputs, please do checks yourself to provide detailed error messages


public class EconomyManager {
    private static EconomyPlayerData getPlayer(UUID uuid) {
        return Solstice.playerData.get(uuid).getData(EconomyPlayerData.class);
    }
    public static BooleanWithError addCurrency(UUID player,long amount){
        return getPlayer(player).wallet.addBalance(amount);
    }

    public static void setCurrency(UUID player,long amount){
        getPlayer(player).wallet.setBalance(amount);
    }
    public static BooleanWithError subtractCurrency(UUID player,long amount){
        return getPlayer(player).wallet.subtractBalance(amount);
    }
    public static BooleanWithError transferCurrency(UUID player1,UUID player2,long amount){
        return getPlayer(player1).wallet.transfer(getPlayer(player2).wallet,amount);
    }
    public static long getCurrency(UUID player) {
        return Solstice.playerData.get(player).getData(EconomyPlayerData.class).wallet.getBalance();
    }

}
