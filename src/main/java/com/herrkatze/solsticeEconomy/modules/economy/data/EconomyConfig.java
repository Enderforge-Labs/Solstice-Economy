package com.herrkatze.solsticeEconomy.modules.economy.data;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

@ConfigSerializable
public class EconomyConfig {

    @Comment("Whether or not to truncate integer balances (ex: $5.00 -> $5) ")
    public boolean truncateIntegerBalances = true;
}
