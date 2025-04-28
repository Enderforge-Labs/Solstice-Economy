package com.herrkatze.solsticeEconomy.modules.economy;


import net.minecraft.network.chat.Component;

import static com.herrkatze.solsticeEconomy.modules.economy.EconomyModule.getConfig;

public class CurrencyRenderer {
    public static Component renderCurrency(long cents){
        String centsStr = String.valueOf(cents);
        var config = getConfig();
        centsStr = String.format("%1$3s",centsStr).replace(" ","0");//Pad zeroes left if necessary
        centsStr = new StringBuilder(centsStr).insert(centsStr.length()-2, ".").toString();
        if (centsStr.substring(centsStr.length() - 2).equals("00") && config.truncateIntegerBalances) { // Balance is an integer and config says to truncate it
            centsStr = centsStr.substring(0,centsStr.length()-3);
        }

        return Component.literal(centsStr);
    }
}
