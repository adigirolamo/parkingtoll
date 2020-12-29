package com.adigi.parkingtoll.presentation.controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


public abstract class BaseRestController<T,U> {

    protected ModelMapper modelMapper;

    protected ResponseEntity<U> createResponse(T t, String... args) {

        return new ResponseEntity<>(convertToDto(t,args), HttpStatus.OK);
    }

    protected abstract U convertToDto(T t, String... args);

}
