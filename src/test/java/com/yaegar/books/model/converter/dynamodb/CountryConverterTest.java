package com.yaegar.books.model.converter.dynamodb;

import com.yaegar.books.model.Country;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class CountryConverterTest {

    private CountryConverter sut = new CountryConverter();

    @Test
    public void shouldConvertToNullIfCountryIsNull() {
        //arrange
        final Country country = null;

        //act
        String result = sut.convert(country);

        //assert
        assertNull(result);
    }

    @Test
    public void shouldConvertToStringIfCountryIsNotNull() {
        //arrange
        final Country country = new Country();
        country.setId("1234");
        country.setName("Nigeria");

        //act
        String result = sut.convert(country);

        //assert
        assertEquals("1234|^|Nigeria", result);
    }

    @Test
    public void shouldUnconvertToNullIfStringIsNull() {
        //arrange
        final String country = null;

        //act
        Country result = sut.unconvert(country);

        //assert
        assertNull(result);
    }

    @Test
    public void shouldUnconvertToCountryIfStringIsNotNull() {
        //arrange
        final String country = "1234|^|Nigeria";

        final Country expectedCountry = new Country();
        expectedCountry.setId("1234");
        expectedCountry.setName("Nigeria");

        //act
        Country result = sut.unconvert(country);

        //assert
        assertEquals(expectedCountry, result);
    }
}