package com.yaegar.books;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.yaegar.books.model.Company;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CompanyPersistHandlerTest {
    @Mock
    private Context context;

    @Mock
    private LambdaLogger lambdaLogger;

    @Test
    public void shouldHandleLambdaRequest() {
        //arrange
        Company expectedCompany = new Company();

        CompanyPersistHandler sut = new CompanyPersistHandler();
        when(context.getLogger()).thenReturn(lambdaLogger);
        doNothing().when(lambdaLogger).log("Company: " + expectedCompany);

        //act
        Company actualCompany = sut.handleRequest(expectedCompany, context);

        //assert
        Assert.assertEquals(expectedCompany, actualCompany);
    }
}
