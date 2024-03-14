package com.admin.catalog.domain.category;

import com.admin.catalog.domain.AggregateRoot;

import java.time.Instant;

public class Category extends AggregateRoot<CategoryId> {
    private final String name;
    private final String description;
    private final Boolean isActive;
    private final Instant createdAt;
    private final Instant updatedAt;
    private final Instant deletedAt;


    private Category(
            final CategoryId id,
            final String name,
            final String description,
            final Boolean isActive,
            final Instant createdAt,
            final Instant updatedAt,
            final Instant deletedAt
    ) {
        super(id);
        this.name = name;
        this.description = description;
        this.isActive = isActive;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.deletedAt = deletedAt;
    }

    public static Category newCategory(final String name, final String description, final Boolean isActive) {
        final var uuid = CategoryId.unique();
        final var now = Instant.now();
       return new Category(uuid, name, description, isActive,
               now, now, null);
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Boolean getActive() {
        return isActive;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }
}
