package com.xq.http;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.net.ssl.SSLSession;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HttpImplTest {
    @Mock
    private HttpClient mockHttpClient;
    private HttpImpl httpImpl;

    @Test
    void shouldSendRequestWithDefaultClient() {
        httpImpl = new HttpImpl();
        assertNotEquals(httpImpl.getHttpClient(), null);
    }

    @Test
    void shouldSendRequestWithInjectedClient() throws IOException, InterruptedException {
        //Arrange
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(createMockResponse(200, "SUCCESS".getBytes(StandardCharsets.UTF_8)));

        httpImpl = new HttpImpl(mockHttpClient);
        var req = new RequestImpl();
        req.setUrl("http://localhost:8080/api/v1/accounts");
        req.setMethod("GET");
        req.setQueryParams(Map.of());
        req.setRequestPayload(null);
        Map<String, String> headers = new HashMap<>();
        headers.put(HeaderType.CONTENT_TYPE.get(), ContentType.JSON.get());
        req.setHeaders(headers);

        //Act
        XResponse res = httpImpl.send(req);

        //Assert
        res.ok();
        assertNotEquals(res.getPayloadAsString(), null);
    }

    @Test
    void shouldSendRequestWhenRequestPayloadNotNull() throws IOException, InterruptedException {
        //Arrange
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(createMockResponse(400, null ));
        httpImpl = new HttpImpl(mockHttpClient);
        var req = new RequestImpl();
        req.setUrl("http://localhost:8080/api/v1/accounts");
        req.setMethod("GET");
        req.setQueryParams(Map.of());
        req.setRequestPayload("{}");
        Map<String, String> headers = new HashMap<>();
        headers.put(HeaderType.CONTENT_TYPE.get(), ContentType.JSON.get());
        req.setHeaders(headers);

        //Act
        XResponse res = httpImpl.send(req);

        //Assert
        res.shouldHaveStatus(400);
    }

    @Test
    void shouldNotThrowWhenHttpResponseIsNull() throws IOException, InterruptedException {
        //Arrange
        when(mockHttpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(null);
        httpImpl = new HttpImpl(mockHttpClient);
        var req = new RequestImpl();
        req.setUrl("http://localhost:8080/api/v1/accounts");
        req.setMethod("GET");
        req.setQueryParams(Map.of());
        req.setRequestPayload("{}");
        Map<String, String> headers = new HashMap<>();
        headers.put(HeaderType.CONTENT_TYPE.get(), ContentType.JSON.get());
        req.setHeaders(headers);

        //Act
        XResponse res = httpImpl.send(req);

        //Assert
        assertEquals(res.getStatusCode(), 0);
        assertEquals(res.getPayload(), null);
        assertEquals(res.getHeaders(), null);
    }

    private HttpResponse<byte[]> createMockResponse(int statusCode, byte[] responsePayload) {
        return new HttpResponse<byte[]>() {
            @Override
            public int statusCode() {
                return statusCode;
            }

            @Override
            public HttpRequest request() {
                return HttpRequest.newBuilder().build();
            }

            @Override
            public Optional<HttpResponse<byte[]>> previousResponse() {
                return Optional.empty();
            }

            @Override
            public HttpHeaders headers() {
                return HttpHeaders.of(Map.of(), (val, val2) -> !val.isEmpty());
            }

            @Override
            public byte[] body() {
                return responsePayload;
            }

            @Override
            public Optional<SSLSession> sslSession() {
                return Optional.empty();
            }

            @Override
            public URI uri() {
                try {
                    return new URI("http://localhost:8080/api/v1/accounts");
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public HttpClient.Version version() {
                return HttpClient.Version.HTTP_1_1;
            }
        };
    }
}
