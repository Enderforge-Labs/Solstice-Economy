package com.herrkatze.solsticeEconomy.modules.economy.commands;

import com.herrkatze.solsticeEconomy.modules.economy.EconomyModule;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.LongArgumentType;
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

import static com.herrkatze.solsticeEconomy.modules.economy.EconomyManager.*;

public class EconomyAdminCommand extends ModCommand<EconomyModule> {
    public EconomyAdminCommand(EconomyModule module) {
        super(module);
    }

    @Override
    public List<String> getNames() {
        return List.of("economy","eco");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> command(String name) {
        return Commands.literal(name)
                .requires(require("admin",3))
                .then(Commands.literal("add")
                        .requires(require("add",3))
                            .then(Commands.argument("player", StringArgumentType.word())
                                .suggests(LocalGameProfile::suggest)
                                .then(Commands.argument("amount", LongArgumentType.longArg(0))
                                    .executes(context -> executeAdd(
                                            context,
                                            LocalGameProfile.getProfile(context, "player"),
                                            LongArgumentType.getLong(context,"amount")

                                    ))
                                )
                            )
                )
                .then(Commands.literal("set")
                        .requires(require("set",3))
                        .then(Commands.argument("player", StringArgumentType.word())
                                .suggests(LocalGameProfile::suggest)
                                .then(Commands.argument("amount", LongArgumentType.longArg(0))
                                        .executes(context -> executeSet(
                                                context,
                                                LocalGameProfile.getProfile(context, "player"),
                                                LongArgumentType.getLong(context,"amount")

                                        ))
                                )
                        )
                )
                .then(Commands.literal("subtract")
                        .requires(require("subtract",3))
                        .then(Commands.argument("player", StringArgumentType.word())
                                .suggests(LocalGameProfile::suggest)
                                .then(Commands.argument("amount", LongArgumentType.longArg(0))
                                        .executes(context -> executeSubtract(
                                                context,
                                                LocalGameProfile.getProfile(context, "player"),
                                                LongArgumentType.getLong(context,"amount")
                                        ))
                                )
                        )
                );


    }

    private int executeAdd(CommandContext<CommandSourceStack> context, GameProfile player, long amount) {
        addCurrency(player.getId(),amount);
        Map<String, Component> map = Map.of(
                "amount",Component.literal(String.valueOf(amount)),
                "player",Component.literal(player.getName())
        );
        context.getSource().sendSuccess(() -> module.locale().get("addCurrencySuccess",map),true);
        ServerPlayer player1 = context.getSource().getServer().getPlayerList().getPlayer(player.getId());
        if (player1 != null) {
            player1.sendSystemMessage(module.locale().get("currencyAddNotification",map));
        }
        return 1;
    }
    private int executeSet(CommandContext<CommandSourceStack> context, GameProfile player, long amount) {
        setCurrency(player.getId(), amount);
        Map<String, Component> map = Map.of(
                "amount",Component.literal(String.valueOf(amount)),
                "player",Component.literal(player.getName())
        );
        context.getSource().sendSuccess(() -> module.locale().get("setCurrencySuccess",map),true);
        ServerPlayer player1 = context.getSource().getServer().getPlayerList().getPlayer(player.getId());
        if (player1 != null) {
            player1.sendSystemMessage(module.locale().get("currencySetNotification",map));
        }
        return 1;
    }
    private int executeSubtract(CommandContext<CommandSourceStack> context, GameProfile player,long amount) {
        subtractCurrency(player.getId(), amount);
        Map<String, Component> map = Map.of(
                "amount",Component.literal(String.valueOf(amount)),
                "player",Component.literal(player.getName())
        );
        context.getSource().sendSuccess(() -> module.locale().get("subtractCurrencySuccess",map),true);
        ServerPlayer player1 = context.getSource().getServer().getPlayerList().getPlayer(player.getId());
        if (player1 != null) {
            player1.sendSystemMessage(module.locale().get("currencySubtractNotification",map));
        }
        return 1;
    }


}