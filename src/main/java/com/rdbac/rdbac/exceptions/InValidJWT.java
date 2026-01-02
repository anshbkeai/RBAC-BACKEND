package com.rdbac.rdbac.exceptions;

public class InValidJWT extends RuntimeException {
     public InValidJWT() { super("TOKEN-INVALID"); }
    public InValidJWT(String message) { super(message); }
    public InValidJWT(String message, Throwable cause) { super(message, cause); }
}   
