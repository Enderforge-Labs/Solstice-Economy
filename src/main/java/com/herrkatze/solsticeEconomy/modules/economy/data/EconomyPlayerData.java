package com.herrkatze.solsticeEconomy.modules.economy.data;

import com.herrkatze.solsticeEconomy.modules.economy.PlayerWallet;

import java.util.UUID;

public class EconomyPlayerData {
    public long balance = 0;
    public PlayerWallet wallet = null;
    public UUID key = null; // License key to be used by the CC api for economy.
    public int oldActiveTime = 0; // Internal value to handle automatic payment for active time
}
