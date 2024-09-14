package com.admin.catalog.application.category.update;

import com.admin.catalog.application.category.create.CreateCategoryOutput;
import com.admin.catalog.domain.category.Category;

public record UpdateCategoryOutput (
        String id
) {
    public static UpdateCategoryOutput with(String id) {
        return new UpdateCategoryOutput(id);
    }

    public static UpdateCategoryOutput from(final Category aCategory) {
        return new UpdateCategoryOutput(aCategory.getId().getValue());
    }

    public static UpdateCategoryOutput from(final String id) {
        return new UpdateCategoryOutput(id);
    }
}
