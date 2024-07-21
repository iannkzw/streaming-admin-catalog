package com.admin.catalog.application.category.update;

import com.admin.catalog.domain.category.Category;
import com.admin.catalog.domain.category.CategoryGateway;
import com.admin.catalog.domain.category.CategoryId;
import com.admin.catalog.domain.exceptions.DomainException;
import com.admin.catalog.domain.exceptions.NotFoundException;
import com.admin.catalog.domain.validation.Error;
import com.admin.catalog.domain.validation.handler.Notification;
import io.vavr.control.Either;

import java.util.function.Supplier;

import static io.vavr.API.Try;

public class DefaultUpdateCategoryUseCase extends UpdateCategoryUseCase{
    private final CategoryGateway categoryGateway;

    public DefaultUpdateCategoryUseCase(CategoryGateway categoryGateway) {
        this.categoryGateway = categoryGateway;
    }

    @Override
    public Either<Notification, UpdateCategoryOutput> execute(UpdateCategoryCommand aCommand) {
        final var anId = CategoryId.from(aCommand.id());

        final var aCategory = this.categoryGateway.findById(anId)
                .orElseThrow(notFound(anId));


        final var notification = Notification.create();

        aCategory
                .update(aCommand.name(), aCommand.description(), aCommand.isActive())
                .validate(notification);

        return notification.hasErrors() ? Either.left(notification) : update(aCategory);

    }

    private Either<Notification, UpdateCategoryOutput> update(final Category aCategory) {
        return Try(() -> this.categoryGateway.update(aCategory))
                .toEither()
                .bimap(Notification::create, UpdateCategoryOutput::from);
    }

    private Supplier<DomainException> notFound(final CategoryId anId) {
        return () -> NotFoundException.with(Category.class, anId);
    }
}
