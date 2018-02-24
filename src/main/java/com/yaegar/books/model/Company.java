package com.yaegar.books.model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import com.yaegar.books.model.converter.dynamodb.CountryConverter;
import com.yaegar.books.model.converter.dynamodb.MonthDayConverter;

import java.time.MonthDay;
import java.util.Objects;

@DynamoDBTable(tableName = "Company")
public class Company {
    private String uuid;
    private String name;
    private String administrator;
    private String administratorAndName;
    private Country country;
    private String industry;
    private MonthDay financialYearEnd;

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

    public String getAdministrator() {
        return administrator;
    }

    public void setAdministrator(String administrator) {
        this.administrator = administrator;
    }

    @DynamoDBTypeConverted(converter = CountryConverter.class)
    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    @DynamoDBTypeConverted(converter = MonthDayConverter.class)
    public MonthDay getFinancialYearEnd() {
        return financialYearEnd;
    }

    public void setFinancialYearEnd(MonthDay financialYearEnd) {
        this.financialYearEnd = financialYearEnd;
    }

    @DynamoDBRangeKey
    public String getAdministratorAndName() {
        return administratorAndName;
    }

    public void setAdministratorAndName(String administratorAndName) {
        if (administratorAndName == null || administratorAndName.isEmpty()) {
            Objects.requireNonNull(name, "Company name cannot be null");
            if (name.trim().length() == 0) {
                throw new IllegalArgumentException("Company name cannot be empty");
            }

            Objects.requireNonNull(administrator, "Company principal cannot be null");
            if (administrator.trim().length() == 0) {
                throw new IllegalArgumentException("Company principal cannot be empty");
            }

            this.administratorAndName = administrator + "." + name.replace(" ", "").toLowerCase();
        } else {
            this.administratorAndName = administratorAndName;
        }
    }

    @Override
    public String toString() {
        return "Company{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", administrator='" + administrator + '\'' +
                ", administratorAndName='" + administratorAndName + '\'' +
                ", country=" + country +
                ", industry='" + industry + '\'' +
                ", financialYearEnd=" + financialYearEnd +
                '}';
    }
}
