package bdd;

import bdd.ioc.DaggerTestAppGraph;
import bdd.ioc.TestAppGraph;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.util.TableUtils;
import com.amazonaws.services.lambda.runtime.ClientContext;
import com.amazonaws.services.lambda.runtime.CognitoIdentity;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.yaegar.books.CompanyPersistHandler;
import com.yaegar.books.model.Company;
import com.yaegar.books.model.Country;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.time.MonthDay;
import java.util.AbstractMap.SimpleEntry;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;

public class CompanyPersistHandlerSteps {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyPersistHandlerSteps.class);
    private AmazonDynamoDB amazonDynamoDB;
    private DynamoDBMapper dynamoDBMapper;
    private Company expectedCompany;
    private OutputStream output = new ByteArrayOutputStream();
    private final Context context = getContext();

    private CompanyPersistHandler sut;
    private DynamoDBMapperConfig dynamoDBMapperConfig;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        TestAppGraph graph = DaggerTestAppGraph.builder().build();

        amazonDynamoDB = graph.getAmazonDynamoDB();
        dynamoDBMapper = graph.getDynamoDBMapper();
        objectMapper = graph.getObjectMapper();

        String tableName = graph.getConfig().getString("dynamodb.yaegarBooksCompanyTable");

        dynamoDBMapperConfig = graph.getBuilder()
                .withTableNameOverride(new DynamoDBMapperConfig.TableNameOverride(tableName)).build();

        DeleteTableRequest deleteTableRequest = dynamoDBMapper
                .generateDeleteTableRequest(Company.class, dynamoDBMapperConfig);
        TableUtils.deleteTableIfExists(amazonDynamoDB, deleteTableRequest);

        CreateTableRequest createTableRequest = dynamoDBMapper.generateCreateTableRequest(Company.class, dynamoDBMapperConfig);
        createTableRequest.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
        amazonDynamoDB.createTable(createTableRequest);

        try {
            TableUtils.waitUntilActive(amazonDynamoDB, createTableRequest.getTableName());
        } catch (InterruptedException e) {
            LOGGER.error("Could not get table name");
        }

        sut = new CompanyPersistHandler(graph);
    }

    @After
    public void tearDown() {
        DeleteTableRequest deleteTableRequest = dynamoDBMapper
                .generateDeleteTableRequest(Company.class, dynamoDBMapperConfig);
        amazonDynamoDB.deleteTable(deleteTableRequest);
    }

    @Given("^a request to save company with name (\\w+) and principal (\\w+) is received$")
    public void aRequestToSaveCompanyWithNameYaegarAndPrincipalIsReceived(String name, String administrator) throws Throwable {
        expectedCompany = new Company();
        expectedCompany.setName(name);
        expectedCompany.setAdministrator(administrator);
        expectedCompany.setCountry(new Country());
        expectedCompany.setIndustry("Services");
        expectedCompany.setFinancialYearEnd(MonthDay.of(6, 30));
    }

    @When("^the lambda is triggered$")
    public void theLambdaIsTriggered() throws Throwable {
        Map<String, Object> body = Collections.singletonMap("company", expectedCompany);

        Map<String, Object> requestMap = Collections.unmodifiableMap(Stream.of(
                new SimpleEntry<>("request_method", "POST"),
                new SimpleEntry<>("body", body)
        ).collect(Collectors.toMap(SimpleEntry::getKey, SimpleEntry::getValue)));

        sut.handleRequest(new ByteArrayInputStream(objectMapper.writeValueAsString(requestMap).getBytes()), output, context);
    }

    @Then("^the company with uuid and administratorAndName is saved in the database$")
    public void theCompanyWithNameAndAdministratorIsSavedInTheDatabase() throws Throwable {
        Company returnCompany = objectMapper.readValue(output.toString(), Company.class);

        Company actualCompany = dynamoDBMapper.load(
                Company.class, returnCompany.getUuid(), returnCompany.getAdministratorAndName(), dynamoDBMapperConfig);
        assertEquals(returnCompany.getAdministratorAndName(), actualCompany.getAdministratorAndName());
        assertEquals(returnCompany.getUuid(), actualCompany.getUuid());
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
