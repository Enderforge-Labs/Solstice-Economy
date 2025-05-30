package com.herrkatze.solsticeEconomy.modules.economy.commands;

import com.herrkatze.solsticeEconomy.modules.economy.*;
import com.herrkatze.solsticeEconomy.modules.economy.data.EconomyLocale;
import com.herrkatze.solsticeEconomy.modules.economy.data.EconomyPlayerData;
import com.herrkatze.solsticeEconomy.modules.economy.integration.computercraft.CCEvents;
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
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.herrkatze.solsticeEconomy.modules.economy.CurrencyRenderer.renderCurrency;

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
                .then(Commands.argument("amount", StringArgumentType.word())
                        .then(Commands.argument("metadata",StringArgumentType.greedyString())
                                .executes(
                                        context -> executePay(
                                                context,
                                                LocalGameProfile.getProfile(context,"player"),
                                                CurrencyParser.getCentsArgument(context,"amount"),
                                                StringArgumentType.getString(context,"metadata")
                                        )
                                ))
                    .executes(context -> executePay(
                            context,
                            LocalGameProfile.getProfile(context,"player"),
                            CurrencyParser.getCentsArgument(context,"amount"),
                            null // I have to put this here because Java sucks
                    )))

        );
    }
    private int executePay(CommandContext<CommandSourceStack> context, GameProfile player2, long amount, @Nullable String metadata) {
        ServerPlayer player1 = context.getSource().getPlayer();
        if (player1 == null) {
            context.getSource().sendFailure(Component.literal("This command must be ran by a player"));
            return 0;
        }
        if (player1.getUUID() == player2.getId()) {
            context.getSource().sendFailure(module.locale().get("noSelfPay"));
            return 0;
        }
        if (amount <= 0) {
            context.getSource().sendFailure(module.locale().get("positiveAmountRequired"));
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
        if(EconomyModule.isCCPresent()) {
            CCEvents.fireEvent(player2.getId(),"player_pay",player1.getName().getString(),(double) EconomyManager.getCurrency(player2.getId()) / 100d,(double) amount/100d,CurrencyRenderer.renderCurrency(EconomyManager.getCurrency(player2.getId())).getString(),CurrencyRenderer.renderCurrency(amount).getString(),metadata);
        }
        Map<String,Component> map = Map.of(
                "player", Component.literal(player2.getName()),
                "amount", renderCurrency(amount)
        );
        context.getSource().sendSuccess(() -> module.locale().get("paySuccess",map),false);
        ServerPlayer player2Entity = context.getSource().getServer().getPlayerList().getPlayer(player2.getId());
        if (player2Entity != null) {
            NotificationManager.sendNotification(PlayerBalanceNotifications.PayNotification(amount,player1),player2Entity);
        }
        return 1;
    }
}
