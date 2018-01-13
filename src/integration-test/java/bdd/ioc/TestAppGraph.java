package bdd.ioc;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.yaegar.books.ioc.Graph;
import dagger.Component;

import javax.inject.Singleton;

@Singleton
@Component(modules = TestAppModule.class)
public interface TestAppGraph extends Graph {
    AmazonDynamoDB getAmazonDynamoDB();
    DynamoDBMapper getDynamoDBMapper();
}
