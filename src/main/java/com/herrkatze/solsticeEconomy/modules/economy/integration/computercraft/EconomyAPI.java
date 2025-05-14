package com.herrkatze.solsticeEconomy.modules.economy.integration.computercraft;

import dan200.computercraft.api.lua.*;

import java.util.UUID;

public class EconomyAPI implements ILuaAPI {
    private final IComputerSystem computer;
    public EconomyAPI(IComputerSystem computer) {
        this.computer = computer;
    }

    @Override
    public String[] getNames() {
        var names = new String[1];
        names[0] = "economy";
        return names;
    }

    @LuaFunction
    public final CCWallet getWallet(IArguments arguments) throws LuaException {
        //Solstice.LOGGER.info("Test CC API");
        String walletKey = arguments.getString(0);
        UUID pkey = UUID.fromString(walletKey);
        return new CCWallet(pkey);
    }
    @LuaFunction
    public final void requestEvents(IArguments arguments) throws LuaException {
        String walletKey = arguments.getString(0);
        UUID pkey = UUID.fromString(walletKey);
        CCEvents.addComputer(pkey,computer);
    }
    public void shutdown(){
        CCEvents.removeComputer(computer);
    }
}
