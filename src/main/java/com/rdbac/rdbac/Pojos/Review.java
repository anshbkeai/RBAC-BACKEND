package com.rdbac.rdbac.Pojos;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.rdbac.rdbac.Pojos.Enums.ReviewContext;
import com.rdbac.rdbac.Pojos.Enums.ReviewLearing;
import com.rdbac.rdbac.Pojos.Enums.ReviewUnderstanding;

import lombok.Builder;
import lombok.Data;

@Document
@Data
@Builder
public class Review {

    @Id
    private ObjectId id;

    
    private String context;
    private String understanding;
    private String learning;
    private Integer score;
    private String text;
}
