package com.xq.http;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.http.HttpHeaders;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ResponseImplTest {
    private ResponseImpl resImpl;

    @BeforeEach
    void setup() {
        resImpl = new ResponseImpl(200, Map.of(), "SUCCESS".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    void from() {

    }

    @Test
    void getPayloadAsStringWhenPayLoadIsNull() {
        resImpl = new ResponseImpl(200, Map.of(), null);
        assertNull(resImpl.getPayloadAsString());
    }

    @Test
    void getPayloadAsStringWhenPayLoadIsNotNull() {
        assertEquals("SUCCESS", resImpl.getPayloadAsString());
    }

    @Test
    void shouldHaveStatus() {
        assertThrows(RuntimeException.class, () -> resImpl.shouldHaveStatus(1));
    }

    @Test
    void shouldHaveStatusDoesNotThrow() {
        assertDoesNotThrow(() -> resImpl.shouldHaveStatus(200));
    }

    @Test
    void shouldMatchPayloadThrow() {
        assertThrows(RuntimeException.class, () -> resImpl.shouldMatchPayload("FAIL"));
    }

    @Test
    void shouldMatchPayloadNotThrow() {
        assertDoesNotThrow(() -> resImpl.shouldMatchPayload("SUCCESS"));
    }

    @Test
    void getStatusCode() {
    }

    @Test
    void getHeaders() {
    }

    @Test
    void getPayload() {
    }
}