package com.yaegar.books.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.Builder;
import com.yaegar.books.model.Company;
import org.junit.Before;
import org.junit.Test;
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

    @Before
    public void setUp() {
        companyDao = new CompanyDaoImpl(dynamoDBMapper);
    }

    @Test
    public void shouldSaveCompany() {
        //arrange
        String name = "Yaegar";
        String tableName = "CompanyTable";

        Company company = new Company();
        company.setName(name);
        when(builder.withTableNameOverride(any())).thenReturn(builder);
        when(builder.build()).thenReturn(dynamoDBMapperConfig);
        doNothing().when(dynamoDBMapper).save(company, dynamoDBMapperConfig);

        //act
        companyDao.save(company, builder, tableName);

        //assert
        verify(dynamoDBMapper, times(1)).save(company, builder.build());
    }
}
