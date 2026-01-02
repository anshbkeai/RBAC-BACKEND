package com.rdbac.rdbac.audit.interfaces;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rdbac.rdbac.audit.application.dto.AuditResponseDto;
import com.rdbac.rdbac.audit.domain.service.AuditService;

import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/audits")
@RequiredArgsConstructor
public class AuditRestController {

    private final AuditService auditService;

    @GetMapping("/{orgId}/view")
    public ResponseEntity<AuditResponseDto> getAudit(
        @PathVariable(required = false) String orgId,
        @RequestParam(required = false) String action, // as we are sending the action as a query paramter we need the Validation for that
        @RequestParam(required = false) String actorUserId,
        @RequestParam(required = false) String targetType,
        @RequestParam(required = false) String resultStatus,
        @RequestParam(required = false) String fromInstant,
        @RequestParam(required = false) String toInstant,
        @RequestParam(required = false) String correlationId,
        @RequestParam(required = false) String cursor,
        @RequestParam(defaultValue = "20") int limit
    ) {

        return ResponseEntity.ok(
            auditService.getAuditlogs(
                orgId,
                action,
                actorUserId,
                targetType,
                resultStatus,
                fromInstant,
                toInstant,
                correlationId,
                cursor,
                limit,
                SecurityContextHolder.getContext().getAuthentication().getName()
            )
        );
    }
    

    @GetMapping("/actions")
    public ResponseEntity<List<String>> getActions() {
        return ResponseEntity.ok(auditService.getAllActions());
    }

    @GetMapping("/targetType")
    public ResponseEntity<List<String>> getTargetTypes() {
        return ResponseEntity.ok(auditService.getAllTargetTypes());
    }
    
}
