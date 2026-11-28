package com.outloob.restaurant.common.util;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
import java.util.UUID;

public final class RequestValueUtils {

    private RequestValueUtils() {
    }

    public static UUID pathUuid(Map<String, String> pathVariables, String name) {
        try {
            return UUID.fromString(pathVariables.get(name));
        } catch (IllegalArgumentException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid path variable: " + name, exception);
        }
    }

    public static String optionalParam(Map<String, String> requestParams, String name) {
        return requestParams.get(name);
    }

    public static String requiredParam(Map<String, String> requestParams, String name) {
        String value = requestParams.get(name);
        if (value == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing request parameter: " + name);
        }
        return value;
    }

    public static double requestDouble(Map<String, String> requestParams, String name) {
        try {
            return Double.parseDouble(requiredParam(requestParams, name));
        } catch (NumberFormatException exception) {
            throw invalidParameter(name, exception);
        }
    }

    public static double requestDouble(Map<String, String> requestParams, String name, double defaultValue) {
        String value = requestParams.get(name);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        try {
            return Double.parseDouble(value);
        } catch (NumberFormatException exception) {
            throw invalidParameter(name, exception);
        }
    }

    public static int requestInt(Map<String, String> requestParams, String name, int defaultValue) {
        String value = requestParams.get(name);
        if (value == null || value.isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException exception) {
            throw invalidParameter(name, exception);
        }
    }

    public static UUID requestUuid(Map<String, String> requestParams, String name) {
        String value = requestParams.get(name);
        if (value == null || value.isEmpty()) {
            return null;
        }
        try {
            return UUID.fromString(value);
        } catch (IllegalArgumentException exception) {
            throw invalidParameter(name, exception);
        }
    }

    private static ResponseStatusException invalidParameter(String name, Exception cause) {
        return new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Invalid request parameter: " + name, cause);
    }
}
