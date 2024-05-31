package com.example.block2.exceptions;

import static java.lang.String.format;

import java.io.Serial;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Entity not found")
public class EntityNotFoundException extends BaseException {

    @Serial
    private static final long serialVersionUID = 1L;

    public EntityNotFoundException(String entity, Long id) {
        super(format("Failed to found '%s' entity  with id: %s", entity, id));
    }
}
