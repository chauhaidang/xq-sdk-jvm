package com.xq.accounts.component;

import com.intuit.karate.Json;
import com.intuit.karate.http.Response;

public class TestDto {
    public record AccountResponse(String name, String email, String mobileNumber, Json reqBody, Response resBody) {
    }
}
