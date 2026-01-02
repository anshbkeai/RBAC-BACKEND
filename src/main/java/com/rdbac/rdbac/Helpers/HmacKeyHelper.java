package com.rdbac.rdbac.Helpers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Base64;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.rdbac.rdbac.exceptions.SecretKeyLoadException;

import lombok.Builder;

@Component
public class HmacKeyHelper {

    private final SecretKey secretKey;

    public HmacKeyHelper(@Value("${JWT_SECRET_BASE64}") String jwtSecretBase64) {
        byte[] decoded = Base64.getDecoder().decode(jwtSecretBase64);
        this.secretKey = new SecretKeySpec(decoded, "HmacSHA256");
    }

    public SecretKey getSecretKey() {
        return secretKey;
    }
}
