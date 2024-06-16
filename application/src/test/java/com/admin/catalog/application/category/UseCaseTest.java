package com.admin.catalog.application.category;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
public abstract class UseCaseTest  {

    @BeforeEach
    public void beforeEach() {
        Mockito.reset(getMocks().toArray());
    }

    protected abstract List<Object> getMocks();
}

