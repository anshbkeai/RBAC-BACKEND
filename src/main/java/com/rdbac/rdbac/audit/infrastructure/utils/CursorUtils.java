package com.rdbac.rdbac.audit.infrastructure.utils;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rdbac.rdbac.audit.domain.model.Cursor;

import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class CursorUtils {

    private final ObjectMapper objectMapper;

    public String encode(Cursor cursor) {
        try {
            String json = objectMapper.writeValueAsString(cursor);
            return java.util.Base64.getEncoder().encodeToString(json.getBytes());
        } catch (Exception e) {
            throw new RuntimeException("Failed to encode cursor", e);
        }
    }

    public Cursor decode(String cursorStr) {
        try {
            byte[] decodedBytes = java.util.Base64.getDecoder().decode(cursorStr);
            String json = new String(decodedBytes);
            return objectMapper.readValue(json, Cursor.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to decode cursor", e);
        }
    }
}
