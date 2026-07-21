package com.kirk;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(MockitoExtension.class)
public class AuxillaryMethodsTest {
    AuxiliaryMethods auxiliaryMethods;

    @Mock
    Scanner mockScanner;

    @BeforeEach
    void setUp(){
        auxiliaryMethods = new AuxiliaryMethods(mockScanner);
    }

    @Test
    void shouldInputChoice(){
        when(mockScanner.nextLine()).thenReturn("2");

        int result = auxiliaryMethods.inputChoice();

        assertEquals(2, result);
    }

    @Nested
    class inputValues{

        @ParameterizedTest
        @CsvSource({
            "5, 5",
            "999, 999"
        })

        void shouldPositiveParser(String a, Integer expected){
            Integer result = auxiliaryMethods.parser(a);

            assertEquals(expected, result);
        }

        @ParameterizedTest
        @CsvSource(value = {
            "null",
            "abc",
            "2.8",
            "true",
            "false"
        }, nullValues = "null")

        void shouldNegativeParser(String a){
            Integer result = auxiliaryMethods.parser(a);

            assertNull(result);
        }

        @ParameterizedTest
        @CsvSource({
            "5, 0, 7",
            "0, 0, 1",
            "7, 8, 1",
            "0, 0, 0"
        })
        
        void shouldPositiveValidator(Integer a, int lower, int upper){
            boolean result = auxiliaryMethods.validator(a, lower, upper);

            assertTrue(result);
        }

        @ParameterizedTest
        @CsvSource(value = {
            "null, 0, 1",
            "92, 1, 6",
            "-1, 1, 7",
            "8, 0, 7"
        }, nullValues = "null")

        void shouldNegativeValidator(Integer a, int lower, int upper){
            boolean result = auxiliaryMethods.validator(a, lower, upper);

            assertFalse(result);
        }
    }
    

    @Nested
    class inputDate{
        @ParameterizedTest
        @CsvSource({
            "12.02.2022, 12, 02, 2022",
            "30.03.2027, 30, 03, 2027",
        }) 

        void shouldPositiveDateParse(String a, Integer day, Integer month, Integer year){
            LocalDate expected = LocalDate.of(year, month, day);

            LocalDate result = auxiliaryMethods.parserDate(a);  

            assertEquals(expected, result);
        }

        @ParameterizedTest
        @CsvSource(value = {
            "2022.12.12",
            "29.02.2025",
            "41.06.2025",
            "15-02-2016",
            "abc",
            "123.3"
        }, nullValues = "null")

        void shouldNegativeDateParse(String a){

            LocalDate result = auxiliaryMethods.parserDate(a);

            assertNull(result);
        }
    }

    @Nested
    class inputNewCompleted{
        @ParameterizedTest
        @CsvSource({
            "y",
            "Y"
        })
        
        void shouldTrueValidatorNewCompleted(String a){
            boolean result = auxiliaryMethods.validatorNewCompleted(a);
            
            assertTrue(result);
        }

        @ParameterizedTest
        @CsvSource({
            "n",
            "N"
        })

        void shouldFalseValidatorNewCompleted(String a){
            boolean result = auxiliaryMethods.validatorNewCompleted(a);

            assertFalse(result);
        }

        @ParameterizedTest
        @CsvSource(value = {
            "Yy",
            "nn",
            "abc",
            "null",
            "1",
            "1234",
            "' '",
            "''"
        }, nullValues = "null")

        void shouldNegativeValidatorNewCompleted(String a){
            Boolean result = auxiliaryMethods.validatorNewCompleted(a);

            assertNull(result);
        }
    }
    
}
