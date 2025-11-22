package com.herrkatze.solsticeEconomy.modules.economy;

import com.snek.frameworklib.utils.Result;
import org.jetbrains.annotations.NotNull;

public class PlayerWallet implements IWallet{
    private long balance;
    @Override
    public long getBalance() {
        return balance;
    }
    public PlayerWallet(long balance){
        this.balance = balance;
    }
    public PlayerWallet(){
        this.balance = 0;
    }

    @Override
    public @NotNull Result<Boolean,String> transfer(IWallet wallet, long amount) {
        if (amount <= 0) {
            return Result.Err("amount must be a positive nonzero long value");
        }
        if (balance < amount) {
            return Result.Err("Not Enough Balance");
        }
        var success = wallet.addBalance(amount);
        if (success.is_ok()) {
            subtractBalance(amount);
        }
        return success;
    }

    @Override
    public @NotNull Result<Boolean, String> addBalance(long amount) {
        if(amount <=0) {
            return Result.Err("amount must be a positive nonzero long value");
        }
        this.balance += amount;
        return Result.Ok(true);
    }

    @Override
    public @NotNull Result<Boolean, String> subtractBalance(long amount) {
        if(amount <=0) {
            return Result.Err("amount must be a positive nonzero long value");
        }
        this.balance -= amount;
        return Result.Ok(true);
    }

    @Override
    public void setBalance(long amount) {
        this.balance = amount;
    }

}
