package com.adigi.parkingtoll.service;

import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.List;

@Service
public class ExceptionService {

    /**
     * Check that t is not null, otherwise throws EntityNotFoundException
     *
     * @param t    entity
     * @param name entity's name
     * @param <T>  entity's type
     */
    public <T> void checkNotNull(T t, String name) {
        if (t == null) {
            throw new EntityNotFoundException(
                    String.format("Not found %s", name));
        }
    }

    /**
     * Check that the list is not null, otherwise throws EntityNotFoundException
     *
     * @param t    list of type T
     * @param name name
     * @param <T>  type
     */
    public <T> void checkNotNull(List<T> t, String name) {
        if (t == null || t.isEmpty()) {
            throw new EntityNotFoundException(
                    String.format("Not found %s", name));
        }
    }
}
