package com.rdbac.rdbac.audit.infrastructure.aspect;

import java.time.Instant;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.MDC;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.rdbac.rdbac.audit.domain.model.Audit;
import com.rdbac.rdbac.audit.domain.model.AuditEvent;
import com.rdbac.rdbac.audit.domain.service.AuditService;
import com.rdbac.rdbac.audit.infrastructure.utils.AudtiEventPublisher;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

// Current design constraint: Due to limitations in the legacy system interface (API-V1), 
// we must currently use generic identifiers (P0, P1, P2) for parameter passing.

// TODO(DASH-305): Replace generic parameter IDs (P0, P1) with descriptive names 
// once the core data model update is complete, as defined in JIRA ticket DASH-305.
@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class AuditEventAspect {

    private final ExpressionParser parser = new SpelExpressionParser();
   // private final AuditService auditService; // we will have the Scale about the RabbitMq arch and then Do that
    private final AudtiEventPublisher audtiEventPublisher;
    @Around("@annotation(audit)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint , Audit audit) throws Throwable {
        Object ret = null ;
        Throwable err= null;
        try {
            ret = proceedingJoinPoint.proceed();
            return ret;
        } catch (Throwable e) {
            err = e;
            throw e;
        }
        finally{
            var ctx = new StandardEvaluationContext();
            var args = proceedingJoinPoint.getArgs();
            var name = proceedingJoinPoint.getSignature().getName();

            // Point for the Current test like to undeertans we are passing the them using P0 P1, but when we click because when we have to scale or we have to go with that level, then we need to actually pass with the different like with their names and that is good one so currently our application doesn't require that feature so we are using this one, but if our application scales or something happens or there's a requirement to do so then we will actually change it
            
            for(int i = 0 ;i < args.length; i++) ctx.setVariable("p"+i, args[i]);
            String orgId = audit.orgId().isBlank()? null : String.valueOf(parser.parseExpression(audit.orgId()).getValue(ctx));
            String targetId = audit.targetId().isBlank()? null : String.valueOf(parser.parseExpression(audit.targetId()).getValue(ctx));
         
            var auth = SecurityContextHolder.getContext().getAuthentication();
            String email = auth!=null? String.valueOf(auth.getName()) : null;

            AuditEvent event = AuditEvent.builder()
                                            .occurredAt(Instant.now())
                                            .orgId(orgId)
                                            .action(audit.action())
                                            .correlationId(MDC.get("correlationId"))
                                            .actor(new AuditEvent.Actor(null, email))
                                            .target(new AuditEvent.Target(audit.targetType(), targetId))
                                            .result(new AuditEvent.Result(err==null?"SUCCESS":"ERROR", null, err==null?null:err.getMessage()))
                                            .build();
            
            audtiEventPublisher.auditPublish(event);
            
            
        }
    }

}
// Aduit Module is Working and like we need to provide about the featrue to them to access that like 