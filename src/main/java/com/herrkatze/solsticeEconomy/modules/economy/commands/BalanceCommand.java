package com.herrkatze.solsticeEconomy.modules.economy.commands;

import com.herrkatze.solsticeEconomy.modules.economy.EconomyManager;
import com.herrkatze.solsticeEconomy.modules.economy.EconomyModule;
import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import me.alexdevs.solstice.api.command.LocalGameProfile;
import me.alexdevs.solstice.api.module.ModCommand;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import java.util.List;
import java.util.Map;

import static com.herrkatze.solsticeEconomy.modules.economy.CurrencyRenderer.renderCurrency;

public class BalanceCommand extends ModCommand<EconomyModule> {
    public BalanceCommand(EconomyModule module) {
        super(module);
    }

    @Override
    public List<String> getNames() {
        return List.of("balance","bal");
    }

    @Override
    public LiteralArgumentBuilder<CommandSourceStack> command(String name) {
        return Commands.literal(name)
            .executes(context -> {
                ServerPlayer player = context.getSource().getPlayer();
                if (player == null) {
                    context.getSource().sendFailure(Component.literal("This command must be ran by a player if no arguments are present."));
                    return 0;
                }
                long balance = EconomyManager.getCurrency(player.getUUID());
                Map<String,Component> map = Map.of("balance", renderCurrency(balance));
                context.getSource().sendSuccess(() -> module.locale().get("balance",map),false);
                return 1;
            })
            .then(Commands.argument("player", StringArgumentType.word())
                .suggests(LocalGameProfile::suggest)
                .requires(require("balance.others",true))
                .executes(context -> {
                    GameProfile profile = LocalGameProfile.getProfile(context,"player");
                    long balance = EconomyManager.getCurrency(profile.getId());
                    Map<String,Component> map = Map.of(
                            "player", Component.literal(profile.getName()),
                            "balance",renderCurrency(balance));
                    context.getSource().sendSuccess(() -> module.locale().get("balanceOthers",map),false);
                    return 1;
                }));
    }
}
