package com.yaegar.books.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Company")
public class Company {
    private String name;
    private String uuid;

    @DynamoDBHashKey
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @DynamoDBRangeKey
    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
