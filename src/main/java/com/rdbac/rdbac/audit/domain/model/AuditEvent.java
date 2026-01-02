package com.rdbac.rdbac.audit.domain.model;

import java.time.Instant;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuditEvent {

    @Id 
private String id;
  private Instant occurredAt;
  private String orgId;
  private String action;
  private String correlationId;
  private Actor actor;
  private Target target;
  private Result result;

  @Data @Builder @NoArgsConstructor @AllArgsConstructor
  public static class Actor { private String userId; private String email; } // use of the Staitc Class is dONE IS that is Most importatu to undeerstand 
  @Data @Builder @NoArgsConstructor @AllArgsConstructor
  public static class Source { private String ip; private String userAgent; private String clientId; }
  @Data @Builder @NoArgsConstructor @AllArgsConstructor
  public static class Target { private String type; private String id; }
  @Data @Builder @NoArgsConstructor @AllArgsConstructor
  public static class Result { private String status; private String errorCode; private String message; }
}


