package com.herrkatze.solsticeEconomy.modules.economy;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.StringUtils;

public class CurrencyParser {
    public static DynamicCommandExceptionType INVALID_CURRENCY = new DynamicCommandExceptionType(o -> Component.literal("Could not parse currency: "+ o));
    public static long getCentsArgument(CommandContext<CommandSourceStack> context,String name) throws CommandSyntaxException { // Only for commands
        String centsStr = StringArgumentType.getString(context,name);
        try {
            return parseCents(centsStr);
        }
        catch (IllegalArgumentException e) {
            throw INVALID_CURRENCY.create(e.getMessage());
        }
    }
    public static long parseCents(String amount) {
        // Strip leading/trailing spaces
        amount = amount.trim();

        // Check for empty string
        if (amount.isEmpty()) {
            throw new IllegalArgumentException("Input is empty");
        }

        for (char c : amount.toCharArray()) {
            if (c != '.') {
                if (!Character.isDigit(c)) {
                    throw new IllegalArgumentException("Invalid character in input: " + amount);
                }
            }
        }
        if (StringUtils.countMatches(amount, '.') > 1) {
            throw new IllegalArgumentException("Multiple decimal points in input: " + amount);
        }

        int dotIndex = amount.indexOf('.');
        String dollarsPart;
        String centsPart;

        if (dotIndex == -1) {
            dollarsPart = amount;
            centsPart = "00";
        } else {
            dollarsPart = amount.substring(0, dotIndex);
            centsPart = amount.substring(dotIndex + 1);

            if (centsPart.length() > 2) {
                throw new IllegalArgumentException("Too many digits in cents part: " + amount);
            }
        }

        if (centsPart.length() == 0) {
            centsPart = "00";
        } else if (centsPart.length() == 1) {
            centsPart = centsPart + "0";
        }

        long dollars = dollarsPart.isEmpty() ? 0 : Long.parseLong(dollarsPart);
        long cents = Long.parseLong(centsPart);

        if (dollars > (Long.MAX_VALUE - cents) / 100) {
            throw new IllegalArgumentException("Total cents amount too large: " + amount);
        }

        return dollars * 100 + cents;

    }
}
