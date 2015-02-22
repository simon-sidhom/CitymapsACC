package com.citymaps.citymapsacc;

class HttpResult {
    private final int statusCode;
    private final String body;

    public HttpResult(int statusCode, String body) {
        this.statusCode = statusCode;
        this.body = body;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getBody() {
        return body;
    }

}
