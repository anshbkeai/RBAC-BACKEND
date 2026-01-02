package com.rdbac.rdbac.audit.application.dto;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data@Builder
public class AuditResponseDto {

    List<AuditDTO> audits;
    String nextCursor;
}
