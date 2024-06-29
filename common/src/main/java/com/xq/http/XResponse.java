package com.xq.http;

import java.util.List;
import java.util.Map;

public interface XResponse {
    void shouldHaveStatus(int status);
    void ok();
    void shouldMatchPayload(String expectedPayload);
    String getPayloadAsString();
    int getStatusCode();
    byte[] getPayload();
    Map<String, List<String>> getHeaders();
}
