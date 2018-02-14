package com.yaegar.books;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.Builder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.typesafe.config.Config;
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
    @Mock
    private Builder builder;
    @Mock
    private Config config;

    @Test
    public void shouldHandleLambdaRequest() {
        //arrange
        String tableName = "CompanyTable";
        Company expectedCompany = new Company();
        expectedCompany.setName("Yaegar");
        expectedCompany.setPrincipal("principal-uuid");
        expectedCompany.setPrincipalAndName(null);

        CompanyPersistHandler sut = new CompanyPersistHandler(graph);
        when(context.getLogger()).thenReturn(lambdaLogger);
        doNothing().when(lambdaLogger).log("Received Company: " + expectedCompany);
        when(graph.getCompanyDao()).thenReturn(companyDao);
        when(graph.getBuilder()).thenReturn(builder);
        when(graph.getConfig()).thenReturn(config);
        when(config.getString("dynamodb.yaegarBooksCompanyTable")).thenReturn(tableName);

        //act
        sut.handleRequest(expectedCompany, context);

        //assert
        verify(companyDao, times(1)).save(expectedCompany, builder, tableName);
    }
}
