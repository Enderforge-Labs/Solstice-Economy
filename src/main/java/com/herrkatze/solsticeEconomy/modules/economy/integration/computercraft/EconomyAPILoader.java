package com.herrkatze.solsticeEconomy.modules.economy.integration.computercraft;

import dan200.computercraft.api.ComputerCraftAPI;

public class EconomyAPILoader {
    public static void LoadEconomyAPI(){
        ComputerCraftAPI.registerAPIFactory(new EconomyAPIFactory());
    }

}
