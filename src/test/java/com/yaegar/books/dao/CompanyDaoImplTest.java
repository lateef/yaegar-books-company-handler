package com.yaegar.books.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.yaegar.books.model.Company;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CompanyDaoImplTest {

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    private CompanyDao companyDao;

    @Before
    public void setUp() {
        companyDao = new CompanyDaoImpl(dynamoDBMapper);
    }

    @Test
    public void shouldSaveCompany() {
        //arrange
        String uuid = UUID.randomUUID().toString();
        String name = "Yaegar";

        Company company = new Company();
        company.setUuid(uuid);
        company.setName(name);
        doNothing().when(dynamoDBMapper).save(company);

        //act
        companyDao.save(company);

        //assert
        verify(dynamoDBMapper, times(1)).save(company);
    }
}
