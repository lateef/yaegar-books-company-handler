package com.yaegar.books;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.yaegar.books.dao.CompanyDao;
import com.yaegar.books.ioc.Graph;
import com.yaegar.books.model.Company;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CompanyPersistHandlerTest {
    @Mock
    private Context context;

    @Mock
    private LambdaLogger lambdaLogger;

    @Mock
    private Graph graph;

    @Mock
    private CompanyDao companyDao;

    @Test
    public void shouldHandleLambdaRequest() {
        //arrange
        Company expectedCompany = new Company();
        expectedCompany.setName("Yaegar");

        CompanyPersistHandler sut = new CompanyPersistHandler(graph);
        when(context.getLogger()).thenReturn(lambdaLogger);
        doNothing().when(lambdaLogger).log("Received Company: " + expectedCompany);
        when(graph.getCompanyDao()).thenReturn(companyDao);

        //act
        sut.handleRequest(expectedCompany, context);

        //assert
        verify(companyDao, times(1)).save(expectedCompany);
    }
}
