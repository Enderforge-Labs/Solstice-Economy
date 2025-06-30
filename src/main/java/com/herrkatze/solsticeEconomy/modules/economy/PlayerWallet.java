package com.herrkatze.solsticeEconomy.modules.economy;

import org.jetbrains.annotations.NotNull;

public class PlayerWallet implements IWallet{
    private long balance;
    @Override
    public long getBalance() {
        return balance;
    }

    @Override
    public @NotNull BooleanWithError transfer(IWallet wallet, long amount) {
        if (amount <= 0) {
            return new BooleanWithError(false,"amount must be a positibe nonzero long value");
        }
        if (balance < amount) {
            return new BooleanWithError(false,"Not Enough Balance");
        }
        var success = wallet.addBalance(amount);
        if (success.get()) {
            subtractBalance(amount);
        }
        return success;
    }

    @Override
    public @NotNull BooleanWithError addBalance(long amount) {
        if(amount <=0) {
            return new BooleanWithError(false,"amount must be a positive nonzero long value");
        }
        this.balance += amount;
        return new BooleanWithError(true);
    }

    @Override
    public @NotNull BooleanWithError subtractBalance(long amount) {
        if(amount <=0) {
            return new BooleanWithError(false,"amount must be a positive nonzero long value");
        }
        this.balance -= amount;
        return new BooleanWithError(true);
    }

    @Override
    public void setBalance(long amount) {
        this.balance = amount;
    }

}
