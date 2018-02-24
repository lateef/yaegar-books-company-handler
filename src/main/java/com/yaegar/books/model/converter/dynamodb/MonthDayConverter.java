package com.yaegar.books.model.converter.dynamodb;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.time.MonthDay;

public class MonthDayConverter implements DynamoDBTypeConverter<String, MonthDay> {
    @Override
    public String convert(MonthDay object) {
        String monthDay = null;

        if (object != null) {
            monthDay = String.join("|^|",
                    object.getMonth().name(),
                    String.valueOf(object.getMonthValue()),
                    String.valueOf(object.getDayOfMonth()));
        }
        return monthDay;
    }

    @Override
    public MonthDay unconvert(String object) {
        if (object != null && object.length() > 0) {
            String[] data = object.split("\\|\\^\\|");
            return MonthDay.of(Integer.parseInt(data[1]), Integer.parseInt(data[2]));
        }
        return null;
    }
}
