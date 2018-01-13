package com.yaegar.books;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.yaegar.books.ioc.DaggerAppGraph;
import com.yaegar.books.ioc.Graph;
import com.yaegar.books.model.Company;

public class CompanyPersistHandler implements RequestHandler<Company, Company> {

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
        graph.getCompanyDao().save(company);
        return company;
    }
}
