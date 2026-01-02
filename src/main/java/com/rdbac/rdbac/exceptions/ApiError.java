package com.rdbac.rdbac.exceptions;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {

    private String message;
    private String errorCode;
    private String path;
    private LocalDateTime timeStamp;


}
