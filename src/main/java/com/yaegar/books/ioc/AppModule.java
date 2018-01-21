package com.yaegar.books.ioc;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import dagger.Module;
import dagger.Provides;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Singleton;

@Module
public class AppModule {
    private static final Logger LOGGER = LoggerFactory.getLogger(AppModule.class);

    @Singleton
    @Provides
    static Config provideConfig() {
        String environment;
        try {
            environment = ConfigFactory.systemEnvironment().getString("appEnvironment");
            LOGGER.info("Using system environment {}", environment);
        } finally {
            try {
                environment = ConfigFactory.systemProperties().getString("app.environment");
                LOGGER.info("Using system property {}", environment);
            } catch (Exception e) {
                // Do nothing, will blow if environment is empty. This is by design
            }
        }
        return ConfigFactory.load("config/".concat(environment.toLowerCase()).concat(".json"));
    }

    @Singleton
    @Provides
    static AmazonDynamoDB provideAmazonDynamoDB() {
        return AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(Regions.EU_WEST_1)
                .build();
    }

    @Singleton
    @Provides
    static DynamoDBMapper provideDynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
        return new DynamoDBMapper(amazonDynamoDB);
    }
}
