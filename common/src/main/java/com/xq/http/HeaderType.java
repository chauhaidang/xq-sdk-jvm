package com.xq.http;

public enum HeaderType {
    CONTENT_TYPE("Content-Type"),
    X_CORRELATION_ID("x-correlation-id"),
    X_STUB_ID("x-stub-id");

    public final String headerType;

    HeaderType(String headerType) {
        this.headerType = headerType;
    }

    public String get() {
        return this.headerType;
    }
}
