package com.rdbac.rdbac.audit.domain.model;

public enum AuditAction {

     
    USER_LOGIN("USER_LOGIN"),
    API_KEY_GENERATED("API-KEY-GENERATED"),
    EMAIL_QUEUED("EMAIL_QUEUED"),
    EMAIL_ACCEPT("EMAIL_ACCEPT"),
    MEMBERSHIP_CREATED("MEMBERSHIP_CREATED"),
    MEMBERSHIP_UPDATED("MEMBERSHIP_UPDATED"),
    ORGANIZATION_CREATED("ORGANIZATION_CREATED"),
    ADD_USER_ORGANISATION("ADD_USER_ORGANISATION"),
    ROLE_PERMISSION_ASSIGN("ROLE_PERMISSION_ASSIGN");
    ;

    private final String label;
    AuditAction(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

   

    
}
