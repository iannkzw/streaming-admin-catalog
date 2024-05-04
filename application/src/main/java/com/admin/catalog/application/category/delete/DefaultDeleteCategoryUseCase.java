package com.admin.catalog.application.category.delete;

import com.admin.catalog.domain.category.CategoryGateway;
import com.admin.catalog.domain.category.CategoryId;

public class DefaultDeleteCategoryUseCase extends DeleteCategoryUseCase {
    private final CategoryGateway categoryGateway;

    public DefaultDeleteCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public void execute(String anIn) {
        final var id = CategoryId.from(anIn);
        this.categoryGateway.deleteById(id);
    }
}
