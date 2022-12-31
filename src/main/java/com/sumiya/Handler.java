package com.sumiya;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;

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
                .withStatusCode(200);
    }

}
