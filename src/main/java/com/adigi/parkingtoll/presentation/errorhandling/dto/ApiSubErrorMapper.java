package com.adigi.parkingtoll.presentation.errorhandling.dto;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.springframework.stereotype.Service;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import javax.validation.ConstraintViolation;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ApiSubErrorMapper {

    public ApiSubError mapValue(FieldError fieldError) {

        return ApiSubError.builder()
                .object(fieldError.getObjectName())
                .field(fieldError.getField())
                .rejectedValue(fieldError.getRejectedValue())
                .message(fieldError.getDefaultMessage())
                .build();
    }

    public ApiSubError mapValue(ObjectError objectError) {

        return ApiSubError.builder()
                .object(objectError.getObjectName())
                .message(objectError.getDefaultMessage())
                .build();
    }

    public ApiSubError mapValue(ConstraintViolation<?> cv) {

        return ApiSubError.builder()
                .object(getApiSubErrorObject(cv))
                .field(((PathImpl) cv.getPropertyPath()).getLeafNode().asString())
                .rejectedValue(cv.getInvalidValue())
                .message(cv.getMessage())
                .build();
    }

    public List<ApiSubError> mapObjectErrors(List<ObjectError> list) {

        if(list==null)
        {
            return new ArrayList<ApiSubError>();
        }

        return mapValues(list, this::mapValue);
    }

    public List<ApiSubError> mapConstrainViolations(Set<ConstraintViolation<?>> set) {

        List<ConstraintViolation<?>> list =
                set != null ?
                new ArrayList<ConstraintViolation<?>>(set)
                : new ArrayList<ConstraintViolation<?>>();

        return mapValues(list, this::mapValue);
    }

    public List<ApiSubError> mapFieldErrors(List<FieldError> list) {

        if(list==null)
        {
            return new ArrayList<ApiSubError>();
        }

        return mapValues(list, this::mapValue);
    }

    //

    private <T> List<ApiSubError> mapValues(List<T> list, Function<T, ApiSubError> mapFunction) {

        return list.stream().filter(s -> s != null).map(s -> mapFunction.apply(s)).collect(Collectors.toList());
    }

    private String getApiSubErrorObject(ConstraintViolation<?> cv) {
        return Optional.ofNullable(cv.getRootBeanClass())
                .map(s -> s.getSimpleName())
                .orElse("");
    }

    private String getApiSubErrorField(ConstraintViolation<?> cv) {
        return Optional.ofNullable(cv.getPropertyPath())
                .filter(s -> s instanceof PathImpl)
                .map(s -> ((PathImpl) s).getLeafNode())
                .map(s -> s.asString())
                .orElse("");
    }

}
