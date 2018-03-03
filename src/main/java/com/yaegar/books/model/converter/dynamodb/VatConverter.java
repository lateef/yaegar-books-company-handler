package com.yaegar.books.model.converter.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;
import com.yaegar.books.model.Vat;

public class VatConverter implements DynamoDBTypeConverter<String, Vat> {
    @Override
    public String convert(Vat object) {
        String vat = null;

        if (object != null) {
            vat = String.join("|^|", object.getNumber().toString(), object.getScheme());
        }
        return vat;
    }

    @Override
    public Vat unconvert(String object) {
        Vat vat = null;

        if (object != null && object.length() > 0) {
            vat = new Vat();
            String[] data = object.split("\\|\\^\\|");
            vat.setNumber(Integer.valueOf(data[0]));
            vat.setScheme(data[1]);
        }
        return vat;
    }
}
