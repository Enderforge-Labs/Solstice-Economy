package com.herrkatze.solsticeEconomy.modules.economy;

import me.alexdevs.solstice.Solstice;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Map;

import static com.herrkatze.solsticeEconomy.modules.economy.CurrencyRenderer.renderCurrency;

public class PlayerBalanceNotifications {
    private static EconomyModule getModule(){
        return Solstice.modules.getModule(EconomyModule.class);
    }
    public static Notification PayNotification(long amount, ServerPlayer player) {
        var module = getModule();
        Map<String,Component> map = Map.of(
                "amount", renderCurrency(amount),
                "sender", player.getDisplayName()
        );
        return new Notification(module.locale().get("payReceived",map));
    }
    public static Notification ReceiveNotification(long amount) {
        var module = getModule();
        Map<String,Component> map = Map.of(
                "amount", renderCurrency(amount)
        );
        return new Notification(module.locale().get("genericReceive",map));
    }
    public static Notification EarningNotification(long amount) {
        var module = getModule();
        Map<String,Component> map = Map.of(
                "amount", renderCurrency(amount)
        );
        return new Notification(module.locale().get("timedReceive",map));
    }
    public static Notification RefundNotification(long amount) {
        var module = getModule();
        Map<String,Component> map = Map.of(
                "amount", renderCurrency(amount)
        );
        return new Notification(module.locale().get("refundReceive",map));

    }
}
