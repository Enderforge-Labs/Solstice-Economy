package com.herrkatze.solsticeEconomy.modules.economy;

import org.jetbrains.annotations.Nullable;

public class BooleanWithError {
    private boolean status;
    private String error = null;
    public boolean get(){
        return this.status;
    }
    public String getError(){
        if (!status) {
            return error;
        }
        return null;
    }
    public BooleanWithError(boolean status, @Nullable String error){
        this.status = status;
        if (!status) {
            this.error = error;
        }
    }
    public BooleanWithError(boolean status){
        this.status = status;
    }
}
