package com.herrkatze.solsticeEconomy.modules.economy;

import org.jetbrains.annotations.NotNull;

public interface IWallet {
    long getBalance();
    @NotNull
    BooleanWithError transfer(IWallet wallet,long amount);
    @NotNull
    BooleanWithError addBalance(long amount);
    @NotNull
    BooleanWithError subtractBalance(long amount);
    void setBalance(long amount);
}
