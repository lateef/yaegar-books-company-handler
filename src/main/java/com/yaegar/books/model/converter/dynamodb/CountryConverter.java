package com.yaegar.books.model.converter.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.yaegar.books.model.Country;

public class CountryConverter implements DynamoDBTypeConverter<String, Country> {
    @Override
    public String convert(Country object) {
        String country = null;

        if (object != null) {
            country = String.join("|^|", object.getId(), object.getName());
        }
        return country;
    }

    @Override
    public Country unconvert(String object) {
        Country country = null;

        if (object != null && object.length() > 0) {
            country = new Country();
            String[] data = object.split("\\|\\^\\|");
            country.setId(data[0]);
            country.setName(data[1]);
        }
        return country;
    }
}
