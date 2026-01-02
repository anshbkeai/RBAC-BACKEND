package com.rdbac.rdbac.ApiKey.config;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.springframework.stereotype.Component;

/*
    Doubt whether to add this as a component or not
    If we add this as a component, then we need to understand about reliability in the application context
    If you're not as it as a component, and we need to automatically generate it as per the need of
    Create a dependency out of it

    1.Still not referring not to create a component, but we will discuss about this and whether to a component or not
*/ 

@Component
public class HashGenrator {

    // we will need about the String that we will 
    public String Sha256Hashing(String input) throws NoSuchAlgorithmException {
        MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
        byte[] encoded = messageDigest.digest(input.getBytes());
        StringBuilder hexString  = new StringBuilder();
        for (byte b : encoded) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
        }
        return hexString.toString();
        
    }

    
}
