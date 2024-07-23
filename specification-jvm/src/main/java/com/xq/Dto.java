package com.xq;

import com.intuit.karate.Json;
import com.intuit.karate.http.Response;

public class Dto {
    public record Account(String name, String email, String mobileNumber, Json reqBody, Response resBody) {
    }
}