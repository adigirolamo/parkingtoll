package com.adigi.parkingtoll.presentation.errorhandling.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "404", description = "Ex. of not found response")
    private int status;
    @Schema(example = "Not Found", description = "")
    private String error;
    @Schema(example = "What has not been found", description = "")
    private String message;
    @Schema(example = "request endpoint", description = "")
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
