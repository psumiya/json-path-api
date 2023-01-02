package com.sumiya;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonPathEvaluatorTest {

    private static final String DEFAULT_ROOT = "src/test/resources/";

    private JsonPathEvaluator fixture;

    @BeforeEach
    public void setup() {
        fixture = new JsonPathEvaluator();
    }

    @Test
    public void happyPathWithAllOptions() {
        String json = FileReader.getFileContentAsString(DEFAULT_ROOT + "withAllOptions.json");
        String evaluated = fixture.evaluate(json);
        assertEquals("""
                ["bar1",null]""", evaluated);
    }

    @Test
    public void happyPathWithSomeOptions() {
        String json = FileReader.getFileContentAsString(DEFAULT_ROOT + "withSomeOptions.json");
        String evaluated = fixture.evaluate(json);
        assertEquals("""
                ["bar1",null]""", evaluated);
    }

    @Test
    public void happyPathWithoutOptions() {
        String json = FileReader.getFileContentAsString(DEFAULT_ROOT + "withoutOptions.json");
        String evaluated = fixture.evaluate(json);
        assertEquals("""
                {"hello":"world"}""", evaluated);
    }

    @Test
    public void withBadPathExpression() {
        String json = FileReader.getFileContentAsString(DEFAULT_ROOT + "badExpression.json");
        String evaluated = fixture.evaluate(json);
        assertEquals("""
                {"message":"Path must not end with a '.' or '..'","errorContext":{"context":"Unexpected error occurred while evaluating jsonPath."}}""", evaluated);
    }

}
