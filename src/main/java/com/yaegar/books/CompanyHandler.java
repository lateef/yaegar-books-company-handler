package com.yaegar.books;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.typesafe.config.ConfigException;
import com.typesafe.config.ConfigFactory;
import com.yaegar.books.ioc.DaggerAppGraph;
import com.yaegar.books.ioc.Graph;
import com.yaegar.books.model.Company;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class CompanyHandler implements RequestStreamHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompanyHandler.class);
    private Graph graph;

    public CompanyHandler() {
        graph = DaggerAppGraph.builder().build();
    }

    public CompanyHandler(Graph appGraph) {
        this.graph = appGraph;
    }

    @Override
    public void handleRequest(InputStream input, OutputStream output, Context context) throws IOException {

        final ObjectMapper objectMapper = graph.getObjectMapper();

        JsonNode jsonNode = objectMapper.readValue(input, JsonNode.class);
        context.getLogger().log("Received request and converted to json: " + jsonNode);

        Company company = objectMapper.treeToValue(jsonNode.get("body").get("company"), Company.class);
            context.getLogger().log("Retrieved Company from json: " + company);

        company.setAdministratorAndName(null);

        String tableName = getProperty();
        graph.getCompanyDao().save(company, graph.getBuilder(), tableName);
        context.getLogger().log("Saved Company: " + company);

        OutputStreamWriter writer = new OutputStreamWriter(output, "UTF-8");
        writer.write(objectMapper.writeValueAsString(company));
        writer.close();
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
