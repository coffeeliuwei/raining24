package com.training.framework.core;

import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RequestContext {
    private final HttpExchange exchange;

    public RequestContext(HttpExchange exchange) {
        this.exchange = exchange;
    }

    public String getMethod() {
        return exchange.getRequestMethod();
    }

    public String getPath() {
        return exchange.getRequestURI().getPath();
    }

    public URI getUri(){
        return exchange.getRequestURI();
    }

    public Map<String, String> queryParams() {
        String q = exchange.getRequestURI().getQuery();
        if (q == null || q.isEmpty()) return Collections.emptyMap();
        Map<String, String> map = new HashMap<>();
        for (String p : q.split("&")) {
            String[] kv = p.split("=");
            if (kv.length >= 1) {
                String key = kv[0];
                String val = kv.length > 1 ? kv[1] : "";
                map.put(key, val);
            }
        }
        return map;
    }

    public String readBody() throws IOException {
        InputStream in = exchange.getRequestBody();
        byte[] buf = in.readAllBytes();
        return new String(buf, StandardCharsets.UTF_8);
    }

    public void writeJson(String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(200, bytes.length);
        try(OutputStream os = exchange.getResponseBody()){
            os.write(bytes);
        }
    }

    public void writeText(int status, String text) throws IOException {
        byte[] bytes = text.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "text/plain; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try(OutputStream os = exchange.getResponseBody()){
            os.write(bytes);
        }
    }
}