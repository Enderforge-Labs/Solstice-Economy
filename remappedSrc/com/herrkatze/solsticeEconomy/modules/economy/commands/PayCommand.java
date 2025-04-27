package com.herrkatze.solsticeEconomy.modules.economy.commands;

import com.herrkatze.solsticeEconomy.modules.economy.EconomyManager;
import com.herrkatze.solsticeEconomy.modules.economy.EconomyModule;
import com.herrkatze.solsticeEconomy.modules.economy.data.EconomyLocale;
import com.herrkatze.solsticeEconomy.modules.economy.data.EconomyPlayerData;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.LongArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import me.alexdevs.solstice.Solstice;
import me.alexdevs.solstice.api.command.LocalGameProfile;
import me.alexdevs.solstice.api.module.ModCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class PayCommand extends ModCommand<EconomyModule> {

    public PayCommand(EconomyModule module) {
        super(module);
    }

    @Override
    public List<String> getNames() {
        return List.of("pay");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> command(String name) {
        return Commands.literal(name)
            .then(Commands.argument("player", StringArgumentType.word())
                .suggests(LocalGameProfile::suggest)
                .then(Commands.argument("amount", LongArgumentType.longArg(0))
                    .executes(context -> executePay(
                            context,
                            LocalGameProfile.getProfile(context,"player"),
                            LongArgumentType.getLong(context,"amount")
                    )))
        );
    }
    private int executePay(CommandContext<CommandSourceStack> context, GameProfile player2, long amount) {
        ServerPlayer player1 = context.getSource().getPlayer();
        if (player1 == null) {
            context.getSource().sendFailure(Component.literal("This command must be ran by a player"));
            return 0;
        }
        UUID player1UUID = player1.getUUID();
        long balance = EconomyManager.getCurrency(player1UUID);
        if (balance < amount) {
            context.getSource().sendFailure(module.locale().get("lowBalance"));
            return 0;
        }
        if (!EconomyManager.transferCurrency(player1UUID,player2.getId(),amount)) {
            context.getSource().sendFailure(module.locale().get("unknownError"));
            return 0;
        }
        Map<String,Component> map = Map.of(
                "player", Component.literal(player2.getName()),
                "amount", Component.literal(String.valueOf(amount))
        );
        context.getSource().sendSuccess(() -> module.locale().get("paySuccess",map),false);
        return 1;
    }
}
