package com.admin.catalog.application.category.update;

import com.admin.catalog.application.category.create.CreateCategoryOutput;
import com.admin.catalog.domain.category.Category;
import com.admin.catalog.domain.category.CategoryGateway;
import com.admin.catalog.domain.category.CategoryId;
import com.admin.catalog.domain.validation.Error;
import com.admin.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

import static io.vavr.API.Try;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase{
    private final CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(UpdateCategoryCommand aCommand) {
        final var anId = CategoryId.from(aCommand.id());

        final var aCategory = this.categoryGateway.findById(anId);

        final var notification = Notification.create();

        if (aCategory.isEmpty()) {
            notification.append(new Error("Category not found"));
            return Either.left(notification);
        }

        aCategory.get()
                .update(aCommand.name(), aCommand.description(), aCommand.isActive())
                .validate(notification);

        return notification.hasErrors() ? Either.left(notification) : update(aCategory.get());

    }

    private Either<Notification, UpdateCategoryOutput> update(final Category aCategory) {
        return Try(() -> this.categoryGateway.update(aCategory))
                .toEither()
                .bimap(Notification::create, UpdateCategoryOutput::from);
    }
}
