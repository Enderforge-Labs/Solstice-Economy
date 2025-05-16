package com.herrkatze.solsticeEconomy.modules.economy.integration.computercraft;

import com.herrkatze.solsticeEconomy.SolsticeEconomy;
import com.herrkatze.solsticeEconomy.modules.economy.*;
import com.mojang.authlib.GameProfile;
import dan200.computercraft.api.lua.IArguments;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import me.alexdevs.solstice.Solstice;

import java.util.UUID;

public class CCWallet {
    private final UUID pkey;

    public CCWallet(UUID pkey) {
        this.pkey = pkey;
    }
    @LuaFunction
    public final String getOwner() throws LuaException {
        var ownerUUID = LicenseManager.getOwner(this.pkey);
        var player = Solstice.getUserCache().getByUUID(ownerUUID);
        if (player.isPresent()) {
            return player.get().getName();
        }
        else {
            SolsticeEconomy.LOGGER.error("Invalid wallet object called getOwner. This should never happen. Please report to Herr Katze.");
            throw new LuaException("Invalid Wallet. This should never happen.");
        }
    }
    @LuaFunction
    public final MethodResult getBalance(){
        var ownerUUID = LicenseManager.getOwner(this.pkey);
        var balance = EconomyManager.getCurrency(ownerUUID);
        return MethodResult.of((double) balance / 100d, CurrencyRenderer.renderCurrency(balance).getString()); // Convert to double for CC API.
    }
    @LuaFunction
    public final MethodResult transfer(IArguments arguments) throws LuaException {
        var ownerUUID = LicenseManager.getOwner(this.pkey);
        var player = arguments.getString(0);
        double amount = arguments.getDouble(1);
        long realAmount = (long) (amount * 100);
        UUID targetUUID;
        var target = Solstice.getUserCache().getByName(player);
        if (target.isPresent()) {
             targetUUID = target.get().getId();
        }
        else {
            return MethodResult.of(false,"Player does not exist");
        }
        boolean success = EconomyManager.transferCurrency(ownerUUID,targetUUID,realAmount);
        if(EconomyModule.isCCPresent() && success) {
            var targetProfile = target.get();
            CCEvents.fireEvent(targetUUID,"computer_transfer",targetProfile.getName(),(double) EconomyManager.getCurrency(targetUUID) / 100d,(double) realAmount/100d,CurrencyRenderer.renderCurrency(EconomyManager.getCurrency(targetUUID)).getString(),CurrencyRenderer.renderCurrency(realAmount).getString());
        }
        var playerEntity = Solstice.server.getPlayerList().getPlayer(targetUUID);
        NotificationManager.sendNotification(PlayerBalanceNotifications.ReceiveNotification(realAmount),playerEntity);
        return MethodResult.of(success);
    }
    @LuaFunction
    public final MethodResult transferString(IArguments arguments) throws LuaException {
        var ownerUUID = LicenseManager.getOwner(this.pkey);
        var player = arguments.getString(0);
        var amount = arguments.getString(1);
        long realAmount = CurrencyParser.parseCents(amount);
        UUID targetUUID;
        var target = Solstice.getUserCache().getByName(player);
        if (target.isPresent()) {
            targetUUID = target.get().getId();
        }
        else {
            return MethodResult.of(false,"Player does not exist");
        }
        boolean success = EconomyManager.transferCurrency(ownerUUID,targetUUID,realAmount);
        if(EconomyModule.isCCPresent() && success) {
            var targetProfile = target.get();
            CCEvents.fireEvent(targetUUID,"computer_transfer",targetProfile.getName(),(double) EconomyManager.getCurrency(targetUUID) / 100d,(double) realAmount/100d,CurrencyRenderer.renderCurrency(EconomyManager.getCurrency(targetUUID)).getString(),CurrencyRenderer.renderCurrency(realAmount).getString());
        }
        var playerEntity = Solstice.server.getPlayerList().getPlayer(targetUUID);
        NotificationManager.sendNotification(PlayerBalanceNotifications.ReceiveNotification(realAmount),playerEntity);
        return MethodResult.of(success);
    } @LuaFunction
    public final MethodResult refund(IArguments arguments) throws LuaException {
        var ownerUUID = LicenseManager.getOwner(this.pkey);
        var player = arguments.getString(0);
        double amount = arguments.getDouble(1);
        long realAmount = (long) (amount * 100);
        UUID targetUUID;
        var target = Solstice.getUserCache().getByName(player);
        if (target.isPresent()) {
            targetUUID = target.get().getId();
        }
        else {
            return MethodResult.of(false,"Player does not exist");
        }
        boolean success = EconomyManager.transferCurrency(ownerUUID,targetUUID,realAmount);
        if(EconomyModule.isCCPresent() && success) {
            var targetProfile = target.get();
            CCEvents.fireEvent(targetUUID,"computer_refund",targetProfile.getName(),(double) EconomyManager.getCurrency(targetUUID) / 100d,(double) realAmount/100d,CurrencyRenderer.renderCurrency(EconomyManager.getCurrency(targetUUID)).getString(),CurrencyRenderer.renderCurrency(realAmount).getString());
        }
        var playerEntity = Solstice.server.getPlayerList().getPlayer(targetUUID);
        NotificationManager.sendNotification(PlayerBalanceNotifications.RefundNotification(realAmount),playerEntity);
        return MethodResult.of(success);
    }
    @LuaFunction
    public final MethodResult refundString(IArguments arguments) throws LuaException {
        var ownerUUID = LicenseManager.getOwner(this.pkey);
        var player = arguments.getString(0);
        var amount = arguments.getString(1);
        long realAmount = CurrencyParser.parseCents(amount);
        UUID targetUUID;
        var target = Solstice.getUserCache().getByName(player);
        if (target.isPresent()) {
            targetUUID = target.get().getId();
        }
        else {
            return MethodResult.of(false,"Player does not exist");
        }
        boolean success = EconomyManager.transferCurrency(ownerUUID,targetUUID,realAmount);
        if(EconomyModule.isCCPresent() && success) {
            var targetProfile = target.get();
            CCEvents.fireEvent(targetUUID,"computer_refund",targetProfile.getName(),(double) EconomyManager.getCurrency(targetUUID) / 100d,(double) realAmount/100d,CurrencyRenderer.renderCurrency(EconomyManager.getCurrency(targetUUID)).getString(),CurrencyRenderer.renderCurrency(realAmount).getString());
        }
        var playerEntity = Solstice.server.getPlayerList().getPlayer(targetUUID);
        NotificationManager.sendNotification(PlayerBalanceNotifications.RefundNotification(realAmount),playerEntity);
        return MethodResult.of(success);
    }
}
