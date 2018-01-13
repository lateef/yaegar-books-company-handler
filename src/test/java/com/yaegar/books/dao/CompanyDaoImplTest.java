package com.yaegar.books.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
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

    private CompanyDao companyDao;

    @Before
    public void setUp() {
        companyDao = new CompanyDaoImpl(dynamoDBMapper);
    }

    @Test
    public void shouldSaveCompany() {
        //arrange
        String name = "Yaegar";

        Company company = new Company();
        company.setName(name);
        doNothing().when(dynamoDBMapper).save(company);

        //act
        companyDao.save(company);

        //assert
        verify(dynamoDBMapper, times(1)).save(company);
    }
}
