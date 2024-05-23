package com.admin.catalog.infrastructure.category;


import com.admin.catalog.infrastructure.MySQLGatewayTest;
import com.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryMySQLGateway;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testInjectedDependencies() {
        Assertions.assertNotNull(categoryMySQLGateway);
        Assertions.assertNotNull(categoryRepository);
    }

}
