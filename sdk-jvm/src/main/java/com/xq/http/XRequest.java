package com.xq.http;

import java.util.Map;

public interface XRequest {
    String getUrl();
    String getMethod();
    String getRequestPayload();
    Map<String, String> getHeaders();
    Map<String, String> getQueryParams();
}
