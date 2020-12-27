package com.adigi.parkingtoll.presentation.errorhandling.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class ApiSubError {
    private String object;
    private String field;
    private Object rejectedValue;
    private String message;
}
