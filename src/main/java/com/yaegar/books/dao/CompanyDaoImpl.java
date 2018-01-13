package com.yaegar.books.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.yaegar.books.model.Company;

import javax.inject.Inject;

public class CompanyDaoImpl implements CompanyDao {
    private final DynamoDBMapper dynamoDBMapper;

    @Inject
    public CompanyDaoImpl(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    @Override
    public void save(Company company) {
        dynamoDBMapper.save(company);
    }
}
