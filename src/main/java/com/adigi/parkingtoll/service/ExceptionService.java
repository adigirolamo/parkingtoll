package com.adigi.parkingtoll.service;

import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ExceptionService {

    public <T> void checkNotNull(T t, String name) {
        if (t == null) {
            throw new EntityNotFoundException(
                    String.format("Not found %s", name));
        }
    }

    public <T> void checkNotNull(List<T> t, String name) {
        if (t == null || t.isEmpty()) {
            throw new EntityNotFoundException(
                    String.format("Not found %s", name));
        }
    }
}
