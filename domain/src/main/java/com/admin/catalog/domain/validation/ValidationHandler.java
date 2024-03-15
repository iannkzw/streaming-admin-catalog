package com.admin.catalog.domain.validation;

import java.util.List;

public interface ValidationHandler {
    ValidationHandler append(Error error);
    ValidationHandler append(ValidationHandler handler);
    ValidationHandler validate(Validation validation);

    List<Error> getErrors();

    default boolean hasErrors() {
        return getErrors() != null && !(getErrors().size() == 0);
    }

    public interface  Validation {
        void validate();
    }
}
