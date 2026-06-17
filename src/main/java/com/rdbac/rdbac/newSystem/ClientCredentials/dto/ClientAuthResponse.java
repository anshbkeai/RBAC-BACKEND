package com.rdbac.rdbac.newSystem.ClientCredentials.dto;

import lombok.Builder;

@Builder
public record ClientAuthResponse(String clientID, String clientSecret , boolean viewOnce ) {
} 