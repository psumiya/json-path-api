package com.sumiya;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

import java.util.Map;

public class Handler implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private final JsonPathEvaluator jsonPathEvaluator;

    public Handler() {
        this.jsonPathEvaluator = new JsonPathEvaluator();
    }

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent input, Context context) {
        String result = jsonPathEvaluator.evaluate(input.getBody());
        return new APIGatewayProxyResponseEvent()
                .withBody(result)
                .withHeaders(Map.of(
                        "Access-Control-Allow-Headers", "Content-Type, X-Api-Key",
                        "Access-Control-Allow-Origin", "https://sumiya.page",
                        "Access-Control-Allow-Methods", "OPTIONS, POST"
                ))
                .withStatusCode(200);
    }

}
