package com.yaegar.books;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.yaegar.books.model.Company;

public class CompanyPersistHandler implements RequestHandler<Company, Company> {
    @Override
    public Company handleRequest(Company company, Context context) {
        context.getLogger().log("Company: " + company);
        return company;
    }
}
