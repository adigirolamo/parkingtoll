package com.adigi.parkingtoll.presentation.errorhandling;

import com.adigi.parkingtoll.presentation.errorhandling.dto.ApiError;
import com.adigi.parkingtoll.presentation.errorhandling.dto.ApiSubErrorMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;
import javax.validation.ConstraintViolationException;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private ApiSubErrorMapper mapper;

    // 400

    /**
     * Handle MissingServletRequestParameterException.
     * Triggered when a 'required' request parameter is missing.
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getClass().getName());
        //
        final String error = ex.getParameterName() + " parameter is missing";
        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, error, request);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getHttpStatus());
    }

    /**
     * Handles argument mismatch exception.
     * Ex. Triggered when is passed a wrong value for an enum
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
        log.info(ex.getClass().getName());
        //
        String simpleName = Optional.ofNullable(ex.getRequiredType()).map(Class::getSimpleName).orElse("");
        String message = String.format("Failed to convert value '%s' to '%s'%s",
                ex.getValue(), simpleName, collectToString(ex));


        final ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, message, request);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getHttpStatus());
    }


    /**
     * Handles ConstraintViolationException.
     * Triggered when @Validated fails.
     * Ex. pass "" to a argument tagged with @NotBlank
     *
     * @param ex
     * @param request
     * @return
     */
    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request) {
        log.info(ex.getClass().getName());
        //
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), request);
        add(apiError, ex);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getHttpStatus());
    }

    /**
     * Handle HttpMessageNotReadableException.
     * Triggered when request JSON is malformed.
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getClass().getName());
        //
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), request);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getHttpStatus());
    }

    // 404

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getClass().getName());
        //
        final String error = "No handler found for method " + ex.getHttpMethod() + " and URL " + ex.getRequestURL();

        final ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, error, request);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getHttpStatus());
    }

    @ExceptionHandler({EntityNotFoundException.class})
    public ResponseEntity<Object> handleConstraintViolation(EntityNotFoundException ex, WebRequest request) {
        log.info(ex.getClass().getName());
        //
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, ex.getLocalizedMessage(), request);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getHttpStatus());
    }

    // 405

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getClass().getName());
        //
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods: ");
        if (ex.getSupportedHttpMethods() != null) {
            ex.getSupportedHttpMethods().forEach(t -> builder.append(t).append(" "));
        }

        final ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED, builder.toString(), request);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getHttpStatus());
    }

    // 415

    /**
     * Handles HttpMediaTypeNotSupportedException.
     * Triggered when wrong Content-Type
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return
     */
    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info(ex.getClass().getName());
        //
        final StringBuilder builder = new StringBuilder();
        builder.append(ex.getContentType());
        builder.append(" media type is not supported. Supported media types: ");
        ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(" "));

        final ApiError apiError = new ApiError(HttpStatus.UNSUPPORTED_MEDIA_TYPE, builder.toString(), request);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getHttpStatus());
    }

    // 500

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        log.info(ex.getClass().getName());
        log.error("error", ex);
        //
        final ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), request);
        return new ResponseEntity<>(apiError, new HttpHeaders(), apiError.getHttpStatus());
    }

    // private

    private void add(ApiError apiError, BindingResult bindingResults) {
        if (bindingResults == null) {
            return;
        }

        apiError.addErrors(mapper.mapFieldErrors(bindingResults.getFieldErrors()));
        apiError.addErrors(mapper.mapObjectErrors(bindingResults.getGlobalErrors()));
    }

    private void add(ApiError apiError, ConstraintViolationException ex) {
        if (ex == null) {
            return;
        }
        apiError.addErrors(mapper.mapConstrainViolations(ex.getConstraintViolations()));
    }

    private String collectToString(MethodArgumentTypeMismatchException ex) {

        Optional<Object[]> optional =
                Optional.ofNullable(ex)
                        .map(MethodArgumentTypeMismatchException::getRequiredType)
                        .map(Class::getEnumConstants);

        if (optional.isPresent()) {
            return ", accepted values: " + Arrays.stream(optional.get()).map(Object::toString).collect(Collectors.joining(", "));
        }

        return "";
    }
}
