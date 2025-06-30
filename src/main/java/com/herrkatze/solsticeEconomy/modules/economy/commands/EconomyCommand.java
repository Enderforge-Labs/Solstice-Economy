package com.herrkatze.solsticeEconomy.modules.economy.commands;

import com.herrkatze.solsticeEconomy.SolsticeEconomy;
import com.herrkatze.solsticeEconomy.modules.economy.CurrencyParser;
import com.herrkatze.solsticeEconomy.modules.economy.EconomyModule;
import com.herrkatze.solsticeEconomy.modules.economy.Notification;
import com.herrkatze.solsticeEconomy.modules.economy.NotificationManager;
import com.herrkatze.solsticeEconomy.modules.economy.integration.computercraft.LicenseManager;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import me.alexdevs.solstice.api.command.LocalGameProfile;
import me.alexdevs.solstice.api.module.ModCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.herrkatze.solsticeEconomy.modules.economy.CurrencyRenderer.renderCurrency;
import static com.herrkatze.solsticeEconomy.modules.economy.EconomyManager.*;

public class EconomyCommand extends ModCommand<EconomyModule> {
    public EconomyCommand(EconomyModule module) {
        super(module);
    }

    @Override
    public List<String> getNames() {
        return List.of("economy","eco");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> command(String name) {
        return Commands.literal(name)
                .then(Commands.literal("license")
                        .then(Commands.literal("register")
                                        .executes(this::executeLicenseRegister)
                                )
                        .then(Commands.literal("revoke")
                                .executes(this::executeLicenseRevoke)
                        )
                        .executes(this::executeLicenseReminder))
                .then(Commands.literal("addBalance")
                        .requires(require("addBalance",3))
                            .then(Commands.argument("player", StringArgumentType.word())
                                .suggests(LocalGameProfile::suggest)
                                .then(Commands.argument("amount", StringArgumentType.word())
                                    .executes(context -> executeAdd(
                                            context,
                                            LocalGameProfile.getProfile(context, "player"),
                                            CurrencyParser.getCentsArgument(context,"amount")
                                    ))
                                )
                            )
                )
                .then(Commands.literal("set")
                        .requires(require("set",3))
                        .then(Commands.argument("player", StringArgumentType.word())
                                .suggests(LocalGameProfile::suggest)
                                .then(Commands.argument("amount", StringArgumentType.word())
                                        .executes(context -> executeSet(
                                                context,
                                                LocalGameProfile.getProfile(context, "player"),
                                                CurrencyParser.getCentsArgument(context,"amount")
                                        ))
                                )
                        )
                )
                .then(Commands.literal("subtractBalance")
                        .requires(require("subtractBalance",3))
                        .then(Commands.argument("player", StringArgumentType.word())
                                .suggests(LocalGameProfile::suggest)
                                .then(Commands.argument("amount", StringArgumentType.word())
                                        .executes(context -> executeSubtract(
                                                context,
                                                LocalGameProfile.getProfile(context, "player"),
                                                CurrencyParser.getCentsArgument(context,"amount")
                                        ))
                                )
                        )
                );


    }

    private int executeLicenseRevoke(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player == null){
            context.getSource().sendFailure(Component.literal("This command must be run by a player"));
            return 0;
        }
        if(!EconomyModule.isCCPresent()){
            context.getSource().sendFailure(Component.literal("This feature requires CC:Tweaked"));
            return 0;
        }
        var uuid = player.getUUID();
        LicenseManager.invalidateKey(LicenseManager.getKey(uuid));
        context.getSource().sendSuccess(() ->module.locale().get("licenseRevoke"),false);
        return 1;
    }

    private int executeLicenseRegister(CommandContext<CommandSourceStack> context) {
        ServerPlayer player = context.getSource().getPlayer();
        if (player == null){
            context.getSource().sendFailure(Component.literal("This command must be run by a player"));
            return 0;
        }
        if(!EconomyModule.isCCPresent()){
            context.getSource().sendFailure(Component.literal("This feature requires CC:Tweaked"));
            return 0;
        }
        var uuid = player.getUUID();
        var key = LicenseManager.createLicense(uuid);
        Map<String,Component> map = Map.of(
                "key",Component.literal(key.toString())
        );
        context.getSource().sendSuccess(()-> module.locale().get("licenseKey",map),false);
        return 1;
    }

    private int executeLicenseReminder(CommandContext<CommandSourceStack> context){
        ServerPlayer player = context.getSource().getPlayer();
        if (player == null){
            context.getSource().sendFailure(Component.literal("This command must be run by a player"));
            return 0;
        }
        if(!EconomyModule.isCCPresent()){
            context.getSource().sendFailure(Component.literal("This feature requires CC:Tweaked"));
            return 0;
        }
        var uuid = player.getUUID();
        var key = LicenseManager.getKey(uuid);
        if (key == null) {
            context.getSource().sendFailure(Component.literal("You do not have a license key, use /eco license register"));
            return 0;
        }
        Map<String,Component> map = Map.of(
                "key",Component.literal(key.toString())
        );
        context.getSource().sendSuccess(()-> module.locale().get("licenseKey",map),false);
        return 1;
    }
    private int executeAdd(CommandContext<CommandSourceStack> context, GameProfile player, long amount) {
        addCurrency(player.getId(),amount);
        Map<String, Component> map = Map.of(
                "amount",renderCurrency(amount),
                "player",Component.literal(player.getName())
        );
        context.getSource().sendSuccess(() -> module.locale().get("addCurrencySuccess",map),true);
        ServerPlayer player1 = context.getSource().getServer().getPlayerList().getPlayer(player.getId());
        if (player1 != null) {
            NotificationManager.sendNotification(new Notification(module.locale().get("currencyAddNotification",map)),player1); // Use custom notification here since only the admin command will use this specific message
        }
        return 1;
    }
    private int executeSet(CommandContext<CommandSourceStack> context, GameProfile player, long amount) {
        setCurrency(player.getId(), amount);
        Map<String, Component> map = Map.of(
                "amount",renderCurrency(amount),
                "player",Component.literal(player.getName())
        );
        context.getSource().sendSuccess(() -> module.locale().get("setCurrencySuccess",map),true);
        ServerPlayer player1 = context.getSource().getServer().getPlayerList().getPlayer(player.getId());
        if (player1 != null) {
            NotificationManager.sendNotification(new Notification(module.locale().get("currencySetNotification",map)),player1);
        }
        return 1;
    }
    private int executeSubtract(CommandContext<CommandSourceStack> context, GameProfile player,long amount) {
        subtractCurrency(player.getId(), amount);
        Map<String, Component> map = Map.of(
                "amount", renderCurrency(amount),
                "player",Component.literal(player.getName())
        );
        context.getSource().sendSuccess(() -> module.locale().get("subtractCurrencySuccess",map),true);
        ServerPlayer player1 = context.getSource().getServer().getPlayerList().getPlayer(player.getId());
        if (player1 != null) {
            NotificationManager.sendNotification(new Notification(module.locale().get("currencySubtractNotification",map)),player1);
        }
        return 1;
    }


}