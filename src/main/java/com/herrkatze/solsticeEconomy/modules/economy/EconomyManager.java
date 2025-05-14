package com.herrkatze.solsticeEconomy.modules.economy;

import com.herrkatze.solsticeEconomy.SolsticeEconomy;
import com.herrkatze.solsticeEconomy.modules.economy.data.EconomyPlayerData;
import com.herrkatze.solsticeEconomy.modules.economy.data.EconomyServerData;
import com.herrkatze.solsticeEconomy.modules.economy.integration.computercraft.CCEvents;
import me.alexdevs.solstice.Solstice;

import java.util.UUID;

// Checks here in the manager are meant as sanity checks on the inputs, please do checks yourself to provide detailed error messages


public class EconomyManager {
    public static boolean addCurrency(UUID player,long amount){
        if(amount <=0){
            return false;
        }
        return modifyCurrency(player,amount);
    }
    private static boolean modifyCurrency(UUID player,long deltaBalance) {
        EconomyPlayerData data =  Solstice.playerData.get(player).getData(EconomyPlayerData.class);
        data.balance += deltaBalance;
        if(EconomyModule.isCCPresent()) {
            CCEvents.fireEvent(player,"balance_change",(double) data.balance / 100d,(double) deltaBalance/100d,CurrencyRenderer.renderCurrency(data.balance).getString(),CurrencyRenderer.renderCurrency(deltaBalance).getString());
        }
        return true;
    }

    public static boolean setCurrency(UUID player,long amount){
        EconomyPlayerData data =  Solstice.playerData.get(player).getData(EconomyPlayerData.class);
        data.balance = amount;
        return true;
    }
    public static boolean subtractCurrency(UUID player,long amount){
        if(amount <=0){
            return false;
        }
        return modifyCurrency(player,amount);
    }
    public static boolean transferCurrency(UUID player1,UUID player2,long amount){
        // Transfer <amount> currency from player1 to player2
        if(amount <= 0) {
            return false; // Can't transfer a negative balance for safety against exploits
        }
        long player1Balance = getCurrency(player1);
        if (player1Balance < amount) {
            return false;
        }
        subtractCurrency(player1,amount);
        addCurrency(player2,amount);
        return true;
    }
    public static long getCurrency(UUID player) {
        return Solstice.playerData.get(player).getData(EconomyPlayerData.class).balance;
    }

}
