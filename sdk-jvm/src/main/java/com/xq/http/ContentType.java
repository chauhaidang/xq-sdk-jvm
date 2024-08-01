package com.xq.http;

public enum ContentType {

    JSON("application/json"),
    TEXT("text/plain");

    public final String contentType;

    ContentType(String contentType) {
        this.contentType = contentType;
    }

    public String get() {
        return this.contentType;
    }
}
