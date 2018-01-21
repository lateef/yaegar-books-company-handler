package com.yaegar.books.ioc;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.yaegar.books.dao.CompanyDao;
import com.yaegar.books.dao.CompanyDaoImpl;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class CommonModule {

    @Singleton
    @Provides
    static DynamoDBMapperConfig.Builder provideBuilder() {
        return DynamoDBMapperConfig.builder();
    }

    @Singleton
    @Provides
    CompanyDao provideCompanyDao(DynamoDBMapper dynamoDBMapper) {
        return new CompanyDaoImpl(dynamoDBMapper);
    }
}
