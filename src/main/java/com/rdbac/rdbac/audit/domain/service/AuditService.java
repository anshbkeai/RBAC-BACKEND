package com.rdbac.rdbac.audit.domain.service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.ArrayOperators.In;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rdbac.rdbac.Organisation.Service.Organisation_Memership_Service;
import com.rdbac.rdbac.audit.application.dto.AuditDTO;
import com.rdbac.rdbac.audit.application.dto.AuditResponseDto;
import com.rdbac.rdbac.audit.domain.model.Audit;
import com.rdbac.rdbac.audit.domain.model.AuditAction;
import com.rdbac.rdbac.audit.domain.model.AuditEvent;
import com.rdbac.rdbac.audit.domain.model.AuditTargetType;
import com.rdbac.rdbac.audit.domain.model.Cursor;
import com.rdbac.rdbac.audit.domain.repository.AuditRepository;
import com.rdbac.rdbac.audit.infrastructure.utils.CursorUtils;
import com.rdbac.rdbac.exceptions.MembershipNotFoundException;
import com.rdbac.rdbac.rabbitmq.config.QueuesConfig;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuditService {

    private final AuditRepository auditRepository;
    private final ObjectMapper objectMapper;
    private final CursorUtils cursorUtils;
    private final MongoTemplate mongoTemplate;
    private final Organisation_Memership_Service organisation_Memership_Service;

    @Transactional
    @RabbitListener(queues = QueuesConfig.AUDIT_QUEUE)
    public void addAuditEvent(String event) throws JsonMappingException, JsonProcessingException{
        AuditEvent auditEvent = objectMapper.readValue(event,AuditEvent.class);
        auditRepository.save(auditEvent);
    } 

    public List<AuditEvent> getByCorelationId(String correlationId) {
        return auditRepository.findByCorrelationId(correlationId);
    }
    

    // pointsi s about we are defining abouyt Diffr
    public AuditResponseDto getAuditlogs(
        String orgId, // this is provided in the Path variabble from the Controller
        String action, // as we are sending the action as a query paramter we need the Validation for that
        String actorUserId,
        String targetType,
        String resultStatus,
        String fromInstant,
        String toInstant,
        String correlationId,
        String cursor,
        int limit,
        String user

    ) {

        // if(organisation_Memership_Service.get_org_user_Memberships(orgId, user) == null ) {
        //     throw new MembershipNotFoundException("User is not a member of the organisation");
        // }
        List<Criteria> criterias = new ArrayList<>();

        criterias.add(Criteria.where("orgId").is(orgId));

        if(cursor != null && !cursor.isEmpty()) {
            Cursor cursor2 = cursorUtils.decode(cursor);
            criterias.add ( new Criteria().orOperator(
                Criteria.where("occurredAt").lt(cursor2.getOccurredAt()) , 
                new Criteria().andOperator(
                    Criteria.where("occurredAt").is(cursor2.getOccurredAt()),
                    Criteria.where("_id").lt(cursor2.getId())
                )
            ));
        }

        if (fromInstant != null || toInstant != null) {
           
           
            Criteria c = Criteria.where("occurredAt");
            if (fromInstant != null) {  Instant from = Instant.parse(fromInstant); c = c.gte(from);}
            if (toInstant != null) { Instant to = Instant.parse(toInstant); c = c.lte(to);}
            criterias.add(c);
        }

        if(action != null) {
            criterias.add(Criteria.where("action").is(action));
        }
        if(actorUserId != null) {
            criterias.add(Criteria.where("actor.email").is(actorUserId));
        }
        if(targetType != null) {
            criterias.add(Criteria.where("target.type").is(targetType));
        }
        if(resultStatus != null) {
            criterias.add(Criteria.where("result.status").is(resultStatus));
        }

        if(correlationId != null) {
            criterias.add(Criteria.where("correlationId").is(correlationId));
        }
        Query query = new Query(
            new Criteria().andOperator(criterias.toArray(new Criteria[0]))
        );

        query.with(Sort.by(
            Sort.Order.desc("occurredAt"),
            Sort.Order.desc("_id")
        ));

        query.limit(limit);
        List<AuditEvent> audits = mongoTemplate.find(query, AuditEvent.class);
        List<AuditDTO > auditDTOs = audits.stream()
                                                            .map(audit -> AuditDTO.builder()
                                                                    .occurredAt(audit.getOccurredAt())
                                                                    .orgId(audit.getOrgId())
                                                                    .action(audit.getAction())
                                                                    .correlationId(audit.getCorrelationId())
                                                                    .actor(audit.getActor() != null ? audit.getActor().getEmail() : null)
                                                                    .target(audit.getTarget() != null ? audit.getTarget().getType() : null)
                                                                    .result(audit.getResult() != null ? audit.getResult().getStatus() : null)
                                                                    .build()

                                                            )   .toList();
        

        return AuditResponseDto.builder()
                .audits(auditDTOs)
                .nextCursor(audits.size() == limit ? getCursorString(audits.get(audits.size() - 1)) : null)
                .build();
    }

     public String getCursorString(AuditEvent auditEvent) {
        Cursor cursor = Cursor.builder()
            .id(auditEvent.getId())
            .occurredAt(auditEvent.getOccurredAt())
            .build();
        return cursorUtils.encode(cursor);
    }

    public List<String> getAllActions() {
        AuditAction[] actions = AuditAction.values();
        return Arrays.stream(actions)
                    .map(a -> a.getLabel())
                    .toList();
    }

    public List<String> getAllTargetTypes() {
        AuditTargetType[] targetTypes = AuditTargetType.values();
        return Arrays.stream(targetTypes)
                    .map(AuditTargetType::name)
                    .toList();
    }
}
