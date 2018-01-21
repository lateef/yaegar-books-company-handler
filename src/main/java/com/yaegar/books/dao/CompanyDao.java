package com.yaegar.books.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.Builder;
import com.yaegar.books.model.Company;

public interface CompanyDao {
    void save(Company company, Builder builder, String tableName);
}
