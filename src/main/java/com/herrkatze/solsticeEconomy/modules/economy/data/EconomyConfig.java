package com.herrkatze.solsticeEconomy.modules.economy.data;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
public class EconomyConfig {

    @Comment("Whether or not to truncate integer balances (ex: $5.00 -> $5) ")
    public boolean truncateIntegerBalances = true;
    @Comment("How often to trigger auto payment (in seconds)")
    public int autoPayInterval = 3600;
    @Comment("How much currency to addBalance to a player's balance every interval (in cents, scaled by active time)")
    public int autoPayAmount = 500;
}
