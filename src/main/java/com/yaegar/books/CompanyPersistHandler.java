package com.yaegar.books;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import com.yaegar.books.ioc.DaggerAppGraph;
import com.yaegar.books.ioc.Graph;
import com.yaegar.books.model.Company;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompanyPersistHandler implements RequestHandler<Company, Company> {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyPersistHandler.class);
    private Graph graph;

    public CompanyPersistHandler() {
        graph = DaggerAppGraph.builder().build();
    }

    public CompanyPersistHandler(Graph appGraph) {
        this.graph = appGraph;
    }

    @Override
    public Company handleRequest(Company company, Context context) {
        context.getLogger().log("Received Company: " + company);

        company.setNameAndPrincipal();

        String tableName = getProperty();
        graph.getCompanyDao().save(company, graph.getBuilder(), tableName);
        context.getLogger().log("Saved Company: " + company);
        return company;
    }

    private String getProperty() {
        String tableName = null;
        try {
            tableName = graph.getConfig().getString("dynamodb.yaegarBooksCompanyTable");
            LOGGER.info("Using application config {}", tableName);
        } catch (ConfigException e1) {
            LOGGER.warn("No tableName found using application config");
        } finally {
            try {
                tableName = ConfigFactory.systemEnvironment().getString("dynamodbYaegarBooksCompanyTable");
                LOGGER.info("Using system environment {}", tableName);
            } catch (ConfigException e2) {
                LOGGER.warn("No tableName found using system environment");
            } finally {
                try {
                    tableName = ConfigFactory.systemProperties().getString("dynamodbYaegarBooksCompanyTable");
                    LOGGER.info("Using system property {}", tableName);
                } catch (ConfigException e3) {
                    LOGGER.warn("No tableName found using system properties");
                }
            }
        }
        if (!tableName.isEmpty()) {
            LOGGER.info("Using tableName {}", tableName);
        }
        return tableName;
    }
}
