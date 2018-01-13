package bdd;

import bdd.ioc.DaggerTestAppGraph;
import bdd.ioc.TestAppGraph;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.yaegar.books.CompanyPersistHandler;
import com.yaegar.books.model.Company;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.Assert.assertEquals;

public class CompanyPersistHandlerSteps {

    private DynamoDBMapper dynamoDBMapper;
    private Company expectedCompany;
    private String uuid;
    private final Context context = getContext();

    private CompanyPersistHandler sut;

    @Before
    public void setUp() {
        TestAppGraph graph = DaggerTestAppGraph.builder().build();

        AmazonDynamoDB amazonDynamoDB = graph.getAmazonDynamoDB();
        dynamoDBMapper = graph.getDynamoDBMapper();

        CreateTableRequest req = dynamoDBMapper.generateCreateTableRequest(Company.class);
        req.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
        amazonDynamoDB.createTable(req);

        sut = new CompanyPersistHandler(graph);
    }

    @Given("^a request to save company with name (\\w+) is received$")
    public void aRequestToSaveCompanyWithNameIsReceived(String name) throws Throwable {
        expectedCompany = new Company();
        expectedCompany.setName(name);
    }

    @When("^the lambda is triggered$")
    public void theLambdaIsTriggered() throws Throwable {
        Company actualCompany = sut.handleRequest(expectedCompany, context);
        uuid = actualCompany.getUuid();
    }

    @Then("^the company with uuid and name (\\w+) is saved in the database$")
    public void theCompanyWithNameIsSavedInTheDatabase(String expectedName) throws Throwable {
        Company actualCompany = dynamoDBMapper.load(Company.class, uuid, expectedName);
        assertEquals(expectedName, actualCompany.getName());
        assertEquals(expectedCompany.getUuid(), actualCompany.getUuid());
    }

    private Context getContext() {
        return new Context() {
            @Override
            public String getAwsRequestId() {
                return null;
            }

            @Override
            public String getLogGroupName() {
                return null;
            }

            @Override
            public String getLogStreamName() {
                return null;
            }

            @Override
            public String getFunctionName() {
                return null;
            }

            @Override
            public String getFunctionVersion() {
                return null;
            }

            @Override
            public String getInvokedFunctionArn() {
                return null;
            }

            @Override
            public CognitoIdentity getIdentity() {
                return null;
            }

            @Override
            public ClientContext getClientContext() {
                return null;
            }

            @Override
            public int getRemainingTimeInMillis() {
                return 0;
            }

            @Override
            public int getMemoryLimitInMB() {
                return 0;
            }

            @Override
            public LambdaLogger getLogger() {
                return new LambdaLogger() {
                    @Override
                    public void log(String message) {

                    }

                    @Override
                    public void log(byte[] message) {

                    }
                };
            }
        };
    }
}
