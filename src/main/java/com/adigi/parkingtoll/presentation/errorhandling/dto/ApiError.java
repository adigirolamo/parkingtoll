package com.adigi.parkingtoll.presentation.errorhandling.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
public class ApiError {

    @JsonIgnore
    private HttpStatus httpStatus;
    private int status;
    private String error;
    private String message;
    private String path;
    private List<ApiSubError> subErrors = new ArrayList<>();
    //

    public ApiError() {
        super();
        subErrors = new ArrayList<>();
    }

    public ApiError(HttpStatus httpStatus, String message, WebRequest request) {
        this();
        this.setMessage(message);
        this.setHttpStatus(httpStatus);
        this.setPath(request);
    }

    public void setMessage(String message) {
        this.message = Optional.ofNullable(uniformMessage(message)).orElse("");
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.status = httpStatus.value();
        this.error = httpStatus.getReasonPhrase();
    }

    public void setPath(WebRequest request) {
        this.path = retrievePath(request);
    }

    public void addErrors(List<ApiSubError> errors) {
        subErrors.addAll(errors);
    }

    //

    private String retrievePath(WebRequest request) {
        return ((ServletWebRequest) request).getRequest().getRequestURI().toString();
    }

    private String uniformMessage(String message) {
        return StringUtils.remove(message, "\"");
    }


}
