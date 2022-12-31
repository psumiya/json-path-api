package com.sumiya;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Option;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

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

    public static void main(String[] args) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        Set<Option> alwaysReturnList = Set.of(
                Option.ALWAYS_RETURN_LIST,
                Option.DEFAULT_PATH_LEAF_TO_NULL,
                Option.SUPPRESS_EXCEPTIONS
        );
        System.out.println(objectMapper.writeValueAsString(alwaysReturnList));
    }

}
