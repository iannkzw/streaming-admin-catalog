package com.admin.catalog.application.category.create;

import com.admin.catalog.domain.category.Category;

public record CreateCategoryOutput(
        String id
) {
    public static CreateCategoryOutput with(String id) {
        return new CreateCategoryOutput(id);
    }

    public static CreateCategoryOutput from(final Category aCategory) {
        return new CreateCategoryOutput(aCategory.getId().getValue());
    }

    public static CreateCategoryOutput from(final String id) {
        return new CreateCategoryOutput(id);
    }
}
