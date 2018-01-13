package com.yaegar.books.ioc;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.yaegar.books.dao.CompanyDao;
import com.yaegar.books.dao.CompanyDaoImpl;
import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {

    @Provides
    AmazonDynamoDB provideAmazonDynamoDB() {
        return AmazonDynamoDBClientBuilder
                .standard()
                .withRegion("eu-west-1")
                .build();
    }

    @Provides
    DynamoDBMapper provideDynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
        return new DynamoDBMapper(amazonDynamoDB);
    }

    @Provides
    CompanyDao provideCompanyDao(DynamoDBMapper dynamoDBMapper) {
        return new CompanyDaoImpl(dynamoDBMapper);
    }
}
