package com.xq.http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;

@Getter
public class HttpImpl {
    private final HttpClient httpClient;

    public HttpImpl() {
        httpClient = HttpClient.newHttpClient();
    }

    public HttpImpl(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    public XResponse send(XRequest request) throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(request.getUrl()))
                .method(request.getMethod(),
                        request.getRequestPayload() == null ?
                                HttpRequest.BodyPublishers.noBody() :
                                HttpRequest.BodyPublishers.ofString(request.getRequestPayload())
                )
                .headers(getHeadersFrom(request))
                .build();
        HttpResponse<byte[]> res = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofByteArray());

        return res == null ?
                ResponseImpl.from(0, null, null) :
                ResponseImpl.from(res.statusCode(), res.body(), res.headers().map());
    }

    private String[] getHeadersFrom(XRequest request) {
        var headers = new String[request.getHeaders().size() * 2];
        int i = 0;
        for (Map.Entry<String, String> entry : request.getHeaders().entrySet()) {
            headers[i] = entry.getKey();
            headers[++i] = entry.getValue();
        }
        return headers;
    }
}
