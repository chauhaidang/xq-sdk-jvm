package com.xq.http;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ResponseImpl implements XResponse {
    private int statusCode;
    private Map<String, List<String>> headers;
    private byte[] payload;

    public static ResponseImpl from(int statusCode, byte[] payload, Map<String, List<String>> headers) {
        return new ResponseImpl(statusCode, headers, payload);
    }

    public void ok() {
        shouldHaveStatus(200);
    }

    public String getPayloadAsString() {
        return getPayload() == null ? null : new String(getPayload(), StandardCharsets.UTF_8);
    }

    public void shouldHaveStatus(int i) {
        if (statusCode != i) {
            throw new RuntimeException(String.format("Expected status code %d, but got %d", i, statusCode));
        }
    }

    public void shouldMatchPayload(String expectedPayload) {
        expectedPayload = expectedPayload.replaceAll("\\n", "");
        var actualPayload = getPayloadAsString()
                .replaceAll("\\n", "")
                .replaceAll("\\s+", "");
        if (!actualPayload.equals(expectedPayload)) {
            throw new RuntimeException(String.format("Expected payload '%s', but got '%s'", expectedPayload, actualPayload));
        }
    }
}
