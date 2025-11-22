package com.herrkatze.solsticeEconomy.modules.economy;

import com.snek.frameworklib.utils.Result;
import org.jetbrains.annotations.NotNull;

public interface IWallet {
    long getBalance();
    @NotNull
    Result<Boolean,String> transfer(IWallet wallet, long amount);
    @NotNull
    Result<Boolean,String> addBalance(long amount);
    @NotNull
    Result<Boolean,String> subtractBalance(long amount);
    void setBalance(long amount);
}
