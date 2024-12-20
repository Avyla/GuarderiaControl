package com.guarderia.GuarderiaControl.exception;

public class NinoNotFoundException extends RuntimeException{
    public NinoNotFoundException(String message){
        super(message);
    }
}
