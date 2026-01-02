package com.rdbac.rdbac.audit.domain.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.rdbac.rdbac.audit.domain.model.AuditEvent;
import java.util.List;


@Repository
public interface AuditRepository extends MongoRepository<AuditEvent,String>{
    List<AuditEvent> findByCorrelationId(String correlationId);
}
