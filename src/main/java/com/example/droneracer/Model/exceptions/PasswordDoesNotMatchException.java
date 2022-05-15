package com.example.droneracer.Model.exceptions;

public class PasswordDoesNotMatchException extends RuntimeException{
    public PasswordDoesNotMatchException() {
        super("Password does not match exception");
    }
}
