package com.yaegar.books.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.Builder;
import com.yaegar.books.model.Company;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CompanyDaoImplTest {

    @Mock
    private DynamoDBMapper dynamoDBMapper;
    @Mock
    private DynamoDBMapperConfig dynamoDBMapperConfig;
    @Mock
    private Builder builder;

    private CompanyDao companyDao;
    private final String name = "Yaegar";
    private final String principal = "principal-uuid";
    private Company company;

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Before
    public void setUp() {
        companyDao = new CompanyDaoImpl(dynamoDBMapper);
        company = new Company();
    }

    @Test
    public void shouldThrowExceptionWhenCompanyNameIsNull() {
        //arrange
        company.setPrincipal(principal);

        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Company name cannot be null");

        company.setNameAndPrincipal();
    }

    @Test
    public void shouldThrowExceptionWhenCompanyNameIsEmpty() {
        //arrange
        company.setName("");
        company.setPrincipal(principal);

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Company name cannot be empty");

        company.setNameAndPrincipal();
    }

    @Test
    public void shouldThrowExceptionWhenCompanyPrincipalIsNull() {
        //arrange
        company.setName(name);

        expectedException.expect(NullPointerException.class);
        expectedException.expectMessage("Company principal cannot be null");

        company.setNameAndPrincipal();
    }

    @Test
    public void shouldThrowExceptionWhenCompanyPrincipalIsEmpty() {
        //arrange
        company.setPrincipal("");
        company.setName(name);

        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("Company principal cannot be empty");

        company.setNameAndPrincipal();
    }

    @Test
    public void shouldSaveCompany() {
        //arrange
        company.setName(name);
        company.setPrincipal(principal);
        company.setNameAndPrincipal();
        when(builder.withTableNameOverride(any())).thenReturn(builder);
        when(builder.build()).thenReturn(dynamoDBMapperConfig);
        doNothing().when(dynamoDBMapper).save(company, dynamoDBMapperConfig);

        //act
        String tableName = "CompanyTable";
        companyDao.save(company, builder, tableName);

        //assert
        verify(dynamoDBMapper, times(1)).save(company, builder.build());
    }
}
