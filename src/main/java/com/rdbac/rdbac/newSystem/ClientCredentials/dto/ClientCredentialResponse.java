package com.rdbac.rdbac.newSystem.ClientCredentials.dto;

import java.time.Duration;

public record ClientCredentialResponse(String access_token , Duration duration) {
} 