package bdd;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.local.embedded.DynamoDBEmbedded;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.yaegar.books.dao.CompanyDao;
import com.yaegar.books.dao.CompanyDaoImpl;
import com.yaegar.books.model.Company;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import static org.junit.Assert.assertEquals;

public class CompanyStepDef {

    private DynamoDBMapper dynamoDBMapper;
    private Company company;

    @Before
    public void setUp() {
        System.getProperties().setProperty("sqlite4java.library.path", "build/libs");
        AmazonDynamoDB amazonDynamoDB = DynamoDBEmbedded.create().amazonDynamoDB();
        dynamoDBMapper = new DynamoDBMapper(amazonDynamoDB);
        CreateTableRequest req = dynamoDBMapper.generateCreateTableRequest(Company.class);
        req.setProvisionedThroughput(new ProvisionedThroughput(1L, 1L));
        amazonDynamoDB.createTable(req);
    }

    @Given("^i create a company name with name (\\w+)")
    public void iCreateACompanyWithAName(String name) throws Throwable {
        company = new Company();
        company.setName(name);
    }

    @When("^i save the company")
    public void iSaveTheCompany() throws Throwable {
        CompanyDao companyDao = new CompanyDaoImpl(dynamoDBMapper);
        companyDao.save(company);
    }

    @Then("^i should have a company with name (\\w+) saved in the database$")
    public void iShouldHaveACompanySavedInTheDatabase(String name) throws Throwable {
        Company actualCompany = dynamoDBMapper.load(Company.class, company.getUuid(), name);
        assertEquals(company.getUuid(), actualCompany.getUuid());
    }
}
