package com.rdbac.rdbac.audit.domain.model;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Audit {

    String action();
     String orgId() default "";
    String targetType() default "";      // e.g., "USER"
  String targetId() default ""; 


}
