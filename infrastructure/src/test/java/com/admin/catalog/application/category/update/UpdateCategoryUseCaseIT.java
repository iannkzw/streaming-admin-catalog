package com.admin.catalog.application.category.update;

import com.admin.catalog.IntegrationTest;
import com.admin.catalog.domain.category.Category;
import com.admin.catalog.domain.category.CategoryGateway;
import com.admin.catalog.domain.exceptions.NotFoundException;
import com.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;

import static org.mockito.Mockito.*;

@IntegrationTest
public class UpdateCategoryUseCaseIT {

    @Autowired
    private UpdateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @SpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_shouldReturnCategoryId() {
        final var aCategory =
                Category.newCategory("Film", null, true);

        save(aCategory);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Assertions.assertEquals(1, categoryRepository.count());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualCategory =
                categoryRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());

        Assertions.assertEquals(truncateToSeconds(aCategory.getCreatedAt()), truncateToSeconds(actualCategory.getCreatedAt()));
        Assertions.assertTrue(truncateToSeconds(aCategory.getUpdatedAt()).isBefore(truncateToSeconds(actualCategory.getUpdatedAt())));

        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() {
        final var aCategory =
                Category.newCategory("Film", null, true);

        save(aCategory);

        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();

        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand =
                UpdateCategoryCommand.with(expectedId.getValue(), expectedName, expectedDescription, expectedIsActive);

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        verify(categoryGateway, times(0)).update(any());
    }

    @Test
    public void givenAValidInactivateCommand_whenCallsUpdateCategory_shouldReturnInactiveCategoryId() {
        final var aCategory =
                Category.newCategory("Film", null, true);

        save(aCategory);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedId = aCategory.getId();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Assertions.assertTrue(aCategory.getActive());
        Assertions.assertNull(aCategory.getDeletedAt());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        final var actualCategory =
                categoryRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());

        Assertions.assertEquals(truncateToSeconds(aCategory.getCreatedAt()), truncateToSeconds(actualCategory.getCreatedAt()));
        Assertions.assertTrue(truncateToSeconds(aCategory.getUpdatedAt()).isBefore(truncateToSeconds(actualCategory.getUpdatedAt())));

        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsRandomException_shouldReturnAException() {
        final var aCategory =
                Category.newCategory("Film", null, true);

        save(aCategory);

        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedId = aCategory.getId();
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Gateway error";

        final var aCommand = UpdateCategoryCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        doThrow(new IllegalStateException(expectedErrorMessage))
                .when(categoryGateway).update(any());

        final var notification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, notification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, notification.firstError().message());

        final var actualCategory =
                categoryRepository.findById(expectedId.getValue()).get();

        Assertions.assertEquals(aCategory.getName(), actualCategory.getName());
        Assertions.assertEquals(aCategory.getDescription(), actualCategory.getDescription());
        Assertions.assertEquals(aCategory.getActive(), actualCategory.isActive());

        Assertions.assertEquals(truncateToSeconds(aCategory.getCreatedAt()), truncateToSeconds(actualCategory.getCreatedAt()));
        Assertions.assertEquals(truncateToSeconds(aCategory.getUpdatedAt()), truncateToSeconds(actualCategory.getUpdatedAt()));
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.getDeletedAt());

    }

    @Test
    public void givenACommandWithInvalidID_whenCallsUpdateCategory_shouldReturnNotFoundException() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = false;
        final var expectedId = "123";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Category with ID 123 was not found";

        final var aCommand = UpdateCategoryCommand.with(
                expectedId,
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var actualException =
                Assertions.assertThrows(NotFoundException.class, () -> useCase.execute(aCommand));

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private void save(final Category... aCategory) {
        categoryRepository.saveAllAndFlush(
                Arrays.stream(aCategory)
                        .map(CategoryJpaEntity::from)
                        .toList()
        );
    }

    private Instant truncateToSeconds(Instant instant) {
        return instant.truncatedTo(ChronoUnit.SECONDS);
    }
}
