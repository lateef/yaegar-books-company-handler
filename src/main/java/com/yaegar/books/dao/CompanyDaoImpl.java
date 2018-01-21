package com.yaegar.books.dao;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.Builder;
import com.yaegar.books.model.Company;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.UUID;

public class CompanyDaoImpl implements CompanyDao {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyDaoImpl.class);
    private final DynamoDBMapper dynamoDBMapper;

    @Inject
    public CompanyDaoImpl(DynamoDBMapper dynamoDBMapper) {
        this.dynamoDBMapper = dynamoDBMapper;
    }

    @Override
    public void save(Company company, Builder builder, String tableName) {
        company.setUuid(UUID.randomUUID().toString());
        DynamoDBMapperConfig dynamoDBMapperConfig = builder
                .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(tableName)).build();

        LOGGER.info("Saving company {} to table {}", company, tableName);
        dynamoDBMapper.save(company, dynamoDBMapperConfig);
        LOGGER.info("Saved company {} to table {}", company, tableName);
    }
}
