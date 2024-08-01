package com.xq.http;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@NoArgsConstructor
@Setter
@Getter
public class RequestImpl implements XRequest{
    private String url;
    private String method;
    private String requestPayload;
    private Map<String, String> headers;
    private Map<String, String> queryParams;
}
