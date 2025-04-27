package com.herrkatze.solsticeEconomy.modules.economy.data;

import java.util.Map;

public class EconomyLocale {
    public static final Map<String, String> MODULE = Map.ofEntries(
            Map.entry("addCurrencySuccess", "<green>Added <gold>$${amount}</gold> to ${player}</green>"),
            Map.entry("setCurrencySuccess", "<green>Set ${player}'s balance to <gold>$${amount}</gold></green>"),
            Map.entry("subtractCurrencySuccess", "<green>Subtracted <gold>$${amount}</gold> from ${player}</green>"),
            Map.entry("paySuccess","<green>Paid <gold>$${amount}</gold> to ${player}</green>"),
            Map.entry("payReceived","<green>${sender} paid you <gold>$${amount}</gold></green>"),
            Map.entry("lowBalance","<red>Not enough balance.</red>"),
            Map.entry("unknownError","<red>An unknown error occurred in the transaction.</red>"),
            Map.entry("balance","<green>Balance: <gold>$${balance}</gold></green>"),
            Map.entry("balanceOthers","<green>${player}'s Balance: <gold>$${balance}</gold></green>"),
            Map.entry("currencyAddNotification","<green><gold>$${amount}</gold> was added to your balance by an admin</green>"),
            Map.entry("currencySetNotification","<green>Your balance was set to <gold>$${amount}</gold> by an admin</green>"),
            Map.entry("currencySubtractNotification","<green><gold>$${amount}</gold> was subtracted from your balance by an admin</green>")

    );
}
