package com.admin.catalog.application.category.create;

import com.admin.catalog.domain.category.Category;
import com.admin.catalog.domain.category.CategoryGateway;

import java.util.Objects;

public class DefaultCreateCategoryUseCase extends CreateCategoryUseCase{

    private final CategoryGateway categoryGateway;

    public DefaultCreateCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Override
    public CreateCategoryOutput execute(CreateCategoryCommand aCommand) {
        final Category category = Category.newCategory(aCommand.name(), aCommand.description(), aCommand.isActive());

        return CreateCategoryOutput.from(this.categoryGateway.create(category));
    }
}
