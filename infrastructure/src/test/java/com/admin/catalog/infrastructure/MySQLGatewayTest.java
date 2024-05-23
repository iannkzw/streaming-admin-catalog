package com.admin.catalog.infrastructure;

import com.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.*;
import java.util.Collection;
import java.util.List;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test")
@ComponentScan(
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*MySQLGateway")
        }
)
@DataJpaTest
@ExtendWith(MySQLGatewayTest.CleanUpExtension.class)
public @interface MySQLGatewayTest {

    class CleanUpExtension implements BeforeEachCallback {
        @Override
        public void beforeEach(final ExtensionContext context) {
            final var appContext = SpringExtension.getApplicationContext(context);

            cleanUp(List.of(
                    appContext.getBean(CategoryRepository.class)
            ));
        }

        private void cleanUp(final Collection<CrudRepository> repositories) {
            repositories.forEach(CrudRepository::deleteAll);
        }
    }
}
