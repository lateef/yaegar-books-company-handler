package com.yaegar.books;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.Builder;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.yaegar.books.dao.CompanyDao;
import com.yaegar.books.ioc.Graph;
import com.yaegar.books.model.Company;
import com.yaegar.books.model.Country;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.MonthDay;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
        expectedCompany.setAdministrator("principal-uuid");
        expectedCompany.setCountry(new Country());
        expectedCompany.setIndustry("Services");
        expectedCompany.setFinancialYearEnd(MonthDay.of(6, 30));
        ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();
        Map<String, Object> body = Collections.singletonMap("company", expectedCompany);

        Map<String, Object> requestMap = Collections.unmodifiableMap(Stream.of(
                new AbstractMap.SimpleEntry<>("request_method", "POST"),
                new AbstractMap.SimpleEntry<>("body", body)
        ).collect(Collectors.toMap(AbstractMap.SimpleEntry::getKey, AbstractMap.SimpleEntry::getValue)));

        CompanyPersistHandler sut = new CompanyPersistHandler(graph);
        when(graph.getObjectMapper()).thenReturn(objectMapper);
        when(context.getLogger()).thenReturn(lambdaLogger);
        when(graph.getCompanyDao()).thenReturn(companyDao);
        when(graph.getBuilder()).thenReturn(builder);
        when(graph.getConfig()).thenReturn(config);
        when(config.getString("dynamodb.yaegarBooksCompanyTable")).thenReturn(tableName);

        //act
        try {
            sut.handleRequest(new ByteArrayInputStream(objectMapper.writeValueAsString(requestMap).getBytes()), new ByteArrayOutputStream(), context);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //assert
        verify(companyDao, times(1)).save(any(), eq(builder), eq(tableName));
    }
}
