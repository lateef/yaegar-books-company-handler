package bdd.ioc;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class TestAppModule {

    @Singleton
    @Provides
    static Config provideConfig() {
        return ConfigFactory.load("config/test.json");
    }

    @Singleton
    @Provides
    static AmazonDynamoDB provideAmazonDynamoDB() {
        System.getProperties().setProperty("sqlite4java.library.path", "build/libs");
        return DynamoDBEmbedded.create().amazonDynamoDB();
    }

    @Singleton
    @Provides
    static DynamoDBMapper provideDynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
        return new DynamoDBMapper(amazonDynamoDB);
    }
}
