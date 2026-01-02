package com.rdbac.rdbac.Dto;

import lombok.Data;

@Data

public class ReviewDto {

    private String context;
    private String understanding;
    private String learning;
    private Integer score;
    private String text;
}
