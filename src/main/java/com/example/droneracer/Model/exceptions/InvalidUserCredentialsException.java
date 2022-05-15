package com.example.droneracer.Model.exceptions;

public class InvalidUserCredentialsException extends RuntimeException{

    public InvalidUserCredentialsException() {
        super("Invalid User Credentials");
    }
}
