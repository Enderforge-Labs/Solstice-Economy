package com.herrkatze.solsticeEconomy.modules.economy.data;

import java.util.Map;
import java.util.UUID;

public class EconomyServerData {
    // First UUID is the private key of the wallet
    // Second is the UUID of the player who owns that key
    public Map<UUID,UUID> keyMap = Map.ofEntries();
}
