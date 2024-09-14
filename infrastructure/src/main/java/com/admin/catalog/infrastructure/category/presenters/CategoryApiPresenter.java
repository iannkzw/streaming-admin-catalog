package com.admin.catalog.infrastructure.category.presenters;

import com.admin.catalog.application.category.retrieve.get.CategoryOutput;
import com.admin.catalog.application.category.retrieve.list.CategoryListOutput;
import com.admin.catalog.infrastructure.category.models.CategoryApiOutput;
import com.admin.catalog.infrastructure.category.models.CategoryListResponse;

public interface CategoryApiPresenter {
    static CategoryApiOutput present(final CategoryOutput output) {
        return new CategoryApiOutput(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    static CategoryListResponse present(final CategoryListOutput output) {
        return new CategoryListResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.isActive(),
                output.createdAt(),
                output.deletedAt()
        );
    }
}
