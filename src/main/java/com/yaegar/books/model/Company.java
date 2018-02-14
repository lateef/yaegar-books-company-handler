package com.yaegar.books.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Objects;

@DynamoDBTable(tableName = "Company")
public class Company {
    private String uuid;
    private String name;
    private String principal;
    private String principalAndName;

    @DynamoDBHashKey
    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getPrincipal() {
        return principal;
    }

    public void setPrincipal(String principal) {
        this.principal = principal;
    }

    @DynamoDBRangeKey
    public String getPrincipalAndName() {
        return principalAndName;
    }

    public void setPrincipalAndName(String principalAndName) {
        if (principalAndName == null || principalAndName.isEmpty()) {
            Objects.requireNonNull(name, "Company name cannot be null");
            if (name.trim().length() == 0) {
                throw new IllegalArgumentException("Company name cannot be empty");
            }

            Objects.requireNonNull(principal, "Company principal cannot be null");
            if (principal.trim().length() == 0) {
                throw new IllegalArgumentException("Company principal cannot be empty");
            }

            this.principalAndName = principal + "." + name.toLowerCase();
        } else {
            this.principalAndName = principalAndName;
        }
    }

    @Override
    public String toString() {
        return "Company{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", principal='" + principal + '\'' +
                '}';
    }
}
