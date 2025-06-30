package com.herrkatze.solsticeEconomy.modules.economy;

import java.util.NoSuchElementException;

public class OptionalWithError<T> {
    private T retval= null;
    private String error = null;

    public OptionalWithError(){
    }

    public void setRetval(T retval) {
        if (this.error != null){
            throw new IllegalStateException("You can't set both a return value and an error");
        }
        this.retval = retval;
    }

    public void setError(String error) {
        if (this.error != null){
            throw new IllegalStateException("You can't set both a return value and an error");
        }
        this.error = error;
    }
    public T get(){
        return retval;
    }
    public T getOrThrow(){
        if (this.error != null) {
            throw new NoSuchElementException(error);
        }
        return retval;
    }
    public boolean ok() {
        return retval != null;
    }
    public boolean isValid() { // Returns false if this object is in an invalid state, such as being empty or having both a retval and an error.
        return (retval != null && error == null) || (retval == null && error != null);
    }
    public String getError(){
        return error;
    }
    public String getErrorOrThrow(){ // Mostly for debugging.
        if (this.error == null) {
            throw new NoSuchElementException("There is no error set on this Optional object");
        }
        return error;
    }

}

