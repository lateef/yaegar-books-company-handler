package com.yaegar.books.ioc;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.Builder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.Config;
import com.yaegar.books.dao.CompanyDao;

public interface Graph {
    ObjectMapper getObjectMapper();
    Config getConfig();
    AmazonDynamoDB getAmazonDynamoDB();
    DynamoDBMapper getDynamoDBMapper();
    CompanyDao getCompanyDao();
    Builder getBuilder();
}
