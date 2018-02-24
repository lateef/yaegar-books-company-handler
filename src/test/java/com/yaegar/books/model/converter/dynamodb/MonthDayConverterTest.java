package com.yaegar.books.model.converter.dynamodb;

import org.junit.Test;

import java.time.MonthDay;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class MonthDayConverterTest {

    private MonthDayConverter sut = new MonthDayConverter();

    @Test
    public void shouldConvertToNullIfMonthDayIsNull() {
        //arrange
        final MonthDay monthDay = null;

        //act
        String result = sut.convert(monthDay);

        //assert
        assertNull(result);
    }

    @Test
    public void shouldConvertToStringIfMonthDayIsNotNull() {
        //arrange
        final MonthDay monthDay = MonthDay.of(6, 30);

        //act
        String result = sut.convert(monthDay);

        //assert
        assertEquals("JUNE|^|6|^|30", result);
    }

    @Test
    public void shouldUnconvertToNullIfStringIsNull() {
        //arrange
        final String monthDay = null;

        //act
        MonthDay result = sut.unconvert(monthDay);

        //assert
        assertNull(result);
    }

    @Test
    public void shouldUnconvertToMonthDayIfStringIsNotNull() {
        //arrange
        final String monthDay = "JUNE|^|6|^|30";

        final MonthDay expectedMonthDay = MonthDay.of(6, 30);

        //act
        MonthDay result = sut.unconvert(monthDay);

        //assert
        assertEquals(expectedMonthDay, result);
    }
}