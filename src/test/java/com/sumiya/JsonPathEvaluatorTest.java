package com.sumiya;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class JsonPathEvaluatorTest {

    private static final String DEFAULT_ROOT = "src/test/resources/";

    static Stream<Arguments> testFilesProvider() {
        return Stream.of(
                arguments(DEFAULT_ROOT + "withAllOptions.json", """
                ["bar1",null]"""),
                arguments(DEFAULT_ROOT + "withSomeOptions.json", """
                ["bar1",null]"""),
                arguments(DEFAULT_ROOT + "withoutOptions.json", """
                {"hello":"world"}"""),
                arguments(DEFAULT_ROOT + "missingPath.json", "[]"),
                arguments(DEFAULT_ROOT + "missingPathSuppressException.json", "[]"),
                arguments(DEFAULT_ROOT + "badExpression.json", """
                {"message":"Path must not end with a '.' or '..'","errorContext":{"context":"Unexpected error occurred while evaluating jsonPath."}}""")
        );
    }

    private JsonPathEvaluator fixture;

    @BeforeEach
    public void setup() {
        fixture = new JsonPathEvaluator();
    }

    @ParameterizedTest
    @MethodSource("testFilesProvider")
    public void testSamples(String path, String expectedResult) {
        String json = FileReader.getFileContentAsString(path);
        String evaluated = fixture.evaluate(json);
        assertEquals(expectedResult, evaluated);
    }

}
