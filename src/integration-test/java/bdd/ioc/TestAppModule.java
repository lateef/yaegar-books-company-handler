package bdd.ioc;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.yaegar.books.dao.CompanyDao;
import com.yaegar.books.dao.CompanyDaoImpl;
import dagger.Module;
import dagger.Provides;

import javax.inject.Singleton;

@Module
public class TestAppModule {

    @Provides
    @Singleton
    static AmazonDynamoDB provideAmazonDynamoDB() {
        System.getProperties().setProperty("sqlite4java.library.path", "build/libs");
        return DynamoDBEmbedded.create().amazonDynamoDB();
    }

    @Provides
    @Singleton
    static DynamoDBMapper provideDynamoDBMapper(AmazonDynamoDB amazonDynamoDB) {
        return new DynamoDBMapper(amazonDynamoDB);
    }

    @Provides
    CompanyDao provideCompanyDao(DynamoDBMapper dynamoDBMapper) {
        return new CompanyDaoImpl(dynamoDBMapper);
    }
}
