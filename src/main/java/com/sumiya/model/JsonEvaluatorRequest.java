package com.sumiya.model;

import com.jayway.jsonpath.Option;

import java.util.Set;

public record JsonEvaluatorRequest(String jsonToEvaluate, String pathExpression, Set<Option> jsonPathOptions) {

    public JsonEvaluatorRequest(String jsonToEvaluate, String pathExpression) {
        this(jsonToEvaluate, pathExpression, Set.of());
    }

}
