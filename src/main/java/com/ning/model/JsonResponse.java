package com.ning.model;

import java.util.HashMap;

public class JsonResponse extends HashMap<String, Object> {

    private static final String STATUS_KEY = "status";
    private static final String CODE_KEY = "code";
    private static final String MSG_KEY = "message";
    private static final String INFO_KEY = "info";
    private static final String LOCATION_KEY = "location";
    private static final String AUTH_TOKEN_KEY = "token";
    private static final String SUCCESS = "success";
    private static final String ERROR = "error";

    public JsonResponse(String status, int code, String msg) {
        this.put("status", status);
        this.put("code", code);
        this.put("message", msg);
    }

    public JsonResponse(String status, int code, String msg, String location) {
        this.put("status", status);
        this.put("code", code);
        this.put("message", msg);
        this.put("location", location);
    }

    public JsonResponse addLocation(String location) {
        this.put("location", location);
        return this;
    }

    public JsonResponse add(String key, Object value) {
        if (!"status".equals(key) && !"code".equals(key) && !"message".equals(key) && !"info".equals(key)) {
            this.put(key, value);
            return this;
        } else {
            throw new RuntimeException("Predefine key");
        }
    }

    public JsonResponse addInfo(Object value) {
        this.put("info", value);
        return this;
    }

    public JsonResponse addAuthToken(String token) {
        if (token != null) {
            this.put("token", token);
        }

        return this;
    }

    public static JsonResponse success() {
        return new JsonResponse("success", 200, "");
    }

    public static JsonResponse success(String msg) {
        return new JsonResponse("success", 200, msg);
    }

    public static JsonResponse failure(int code, String msg) {
        return new JsonResponse("error", code, msg);
    }

    public static JsonResponse failure(String msg) {
        return new JsonResponse("error", 0, msg);
    }

    public static JsonResponse success(String msg, String location) {
        return new JsonResponse("success", 200, msg, location);
    }

    public boolean isSuccess() {
        return "success".equals(this.get("status"));
    }

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof JsonResponse)) {
            return false;
        } else {
            JsonResponse other = (JsonResponse) o;
            return other.canEqual(this);
        }
    }

    protected boolean canEqual(Object other) {
        return other instanceof JsonResponse;
    }

    public int hashCode() {
        int result = 1;
        return result;
    }

    public String toString() {
        return "JsonResponse()";
    }

}
