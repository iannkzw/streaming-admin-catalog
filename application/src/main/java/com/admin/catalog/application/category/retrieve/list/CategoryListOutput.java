package com.admin.catalog.application.category.retrieve.list;

import com.admin.catalog.domain.category.Category;
import com.admin.catalog.domain.category.CategoryId;

import java.time.Instant;

public record CategoryListOutput(
        CategoryId id,
        String name,
        String description,
        boolean isActive,
        Instant createdAt,
        Instant deletedAt
) {

    public static CategoryListOutput from(final Category aCategory) {
        return new CategoryListOutput(
                aCategory.getId(),
                aCategory.getName(),
                aCategory.getDescription(),
                aCategory.getActive(),
                aCategory.getCreatedAt(),
                aCategory.getDeletedAt()
        );
    }
}
