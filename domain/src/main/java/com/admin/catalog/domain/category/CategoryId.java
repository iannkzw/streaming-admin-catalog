package com.admin.catalog.domain.category;

import com.admin.catalog.domain.Identifier;

import java.util.Objects;
import java.util.UUID;

public class CategoryId extends Identifier {

    private final String value;
    private CategoryId(final String value) {
        Objects.requireNonNull(value, "value should not be null");
        this.value = value;
    }

    public static CategoryId unique() {
        return new CategoryId(UUID.randomUUID().toString().toLowerCase());
    }

    public static CategoryId from(final String value) {
        return new CategoryId(value);
    }

    public static CategoryId from(final UUID value) {
        return new CategoryId(value.toString().toLowerCase());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CategoryId categoryId = (CategoryId) o;
        return value.equals(categoryId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}
