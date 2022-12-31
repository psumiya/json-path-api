package com.sumiya.model;

import java.util.Map;

public record ErrorResponse(String message, Map<String, Object> errorContext) {
}
