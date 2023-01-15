package com.sumiya;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.cache.CacheProvider;
import com.jayway.jsonpath.spi.cache.NOOPCache;
import com.sumiya.model.ErrorResponse;
import com.sumiya.model.JsonEvaluatorRequest;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class JsonPathEvaluator {

    private static final ObjectMapper DEFAULT_MAPPER = new ObjectMapper();

    static {
        // Disable caching since functions like `$.length()` fail on repeated invocations (v2.7.0).
        // To see how the app fails, comment below line that sets NOOPCache,
        // then run JsonPathEvaluatorTest#testCache method.
        CacheProvider.setCache(new NOOPCache());
    }

    public String evaluate(String body) {

        JsonEvaluatorRequest jsonEvaluatorRequest;
        Optional<Configuration> maybeConfiguration;
        try {
            jsonEvaluatorRequest = DEFAULT_MAPPER.readValue(body, JsonEvaluatorRequest.class);
            Set<Option> jsonPathOptions = jsonEvaluatorRequest.jsonPathOptions();
            maybeConfiguration = getConfiguration(jsonPathOptions);
        } catch (JsonProcessingException e) {
            return buildExceptionResponse(e.getMessage(), Map.of(
                    "context", "Bad input json string received. See spec for request shape."
            ));
        }

        try {
            Object evaluationResult;
            if (maybeConfiguration.isPresent()) {
                evaluationResult = JsonPath
                        .using(maybeConfiguration.get())
                        .parse(jsonEvaluatorRequest.jsonToEvaluate())
                        .read(jsonEvaluatorRequest.pathExpression());
            } else {
                evaluationResult = JsonPath
                        .parse(jsonEvaluatorRequest.jsonToEvaluate())
                        .read(jsonEvaluatorRequest.pathExpression());
            }
            return evaluationResult == null ? "" : DEFAULT_MAPPER.writeValueAsString(evaluationResult);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            return buildExceptionResponse(e.getMessage(), Map.of(
                    "context", "Unexpected error occurred while evaluating jsonPath."
            ));
        }

    }

    private String buildExceptionResponse(String e, Map<String, Object> additionalDetails) {
        ErrorResponse errorResponse = new ErrorResponse(e, additionalDetails);
        try {
            return DEFAULT_MAPPER.writeValueAsString(errorResponse);
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
    }

    private Optional<Configuration> getConfiguration(Set<Option> jsonPathOptions) {
        return Optional.ofNullable(jsonPathOptions)
                .map(options -> Configuration.builder().options(options).build());
    }

}
