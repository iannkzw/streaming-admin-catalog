package com.admin.catalog.infrastructure.api.Controllers;

import com.admin.catalog.application.category.create.CreateCategoryCommand;
import com.admin.catalog.application.category.create.CreateCategoryOutput;
import com.admin.catalog.application.category.create.CreateCategoryUseCase;
import com.admin.catalog.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.admin.catalog.domain.pagination.Pagination;
import com.admin.catalog.domain.validation.handler.Notification;
import com.admin.catalog.infrastructure.api.CategoryAPI;
import com.admin.catalog.infrastructure.category.models.CategoryApiOutput;
import com.admin.catalog.infrastructure.category.models.CreateCategoryRequest;
import com.admin.catalog.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryAPI {

    private final CreateCategoryUseCase createCategoryUseCase;
    private final GetCategoryByIdUseCase getCategoryByIdUseCase;

    public CategoryController(CreateCategoryUseCase createCategoryUseCase,
                              GetCategoryByIdUseCase getCategoryByIdUseCase) {
        this.createCategoryUseCase = createCategoryUseCase;
        this.getCategoryByIdUseCase = getCategoryByIdUseCase;
    }

    @Override
    public ResponseEntity<?> createCategory(@RequestBody CreateCategoryRequest input) {
        final var aCommand = CreateCategoryCommand.with(
                input.name(),
                input.description(),
                input.active() != null ? input.active() : true
        );

        final Function<Notification, ResponseEntity<?>> onError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/categories/" + output.id())).body(output);

        return this.createCategoryUseCase.execute(aCommand)
                .fold(onError, onSuccess);
    }

    @Override
    public CategoryApiOutput getCategoryById(String id) {
        return CategoryApiPresenter.present(this.getCategoryByIdUseCase.execute(id));
    }

    @Override
    public Pagination<?> listCategories(String search, int page, int perPage, String sort, String direction) {
        return null;
    }
}
