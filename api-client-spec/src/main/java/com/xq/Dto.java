package com.xq;

import com.intuit.karate.Json;
import com.intuit.karate.http.Response;

public class Dto {
    public record Account(String name, String email, String mobileNumber, Json reqBody, Response resBody) {
    }
    public record AccountJson(String name, String email, String mobileNumber, Json reqBody, Json resBody, int statusCode) {
    }
}