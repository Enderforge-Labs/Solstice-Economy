package com.herrkatze.solsticeEconomy.modules.economy.integration.computercraft;

import dan200.computercraft.api.lua.IComputerSystem;
import dan200.computercraft.api.lua.ILuaAPI;
import dan200.computercraft.api.lua.ILuaAPIFactory;
import org.jetbrains.annotations.Nullable;

public class EconomyAPIFactory implements ILuaAPIFactory {
    @Override
    public @Nullable ILuaAPI create(IComputerSystem computer) {
        return new EconomyAPI(computer);
    }
}
