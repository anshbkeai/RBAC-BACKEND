package com.rdbac.rdbac.audit.application.dto;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditDTO {

    private Instant occurredAt;
  private String orgId;
  private String action;
  private String correlationId;
  private String actor;
  private String target;
  private String result;

  
    
}
