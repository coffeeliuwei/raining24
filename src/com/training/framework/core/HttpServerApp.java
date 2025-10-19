package com.training.framework.core;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class HttpServerApp {
    private final Router router;
    private final Path webRoot;
    private HttpServer server;

    public HttpServerApp(Router router, Path webRoot){
        this.router = router;
        this.webRoot = webRoot;
    }

    public void start(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 0);
        server.createContext("/api", new ApiHandler());
        server.createContext("/", new StaticHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("Server started at http://localhost:" + port + "/");
    }

    class ApiHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            RequestContext ctx = new RequestContext(exchange);
            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            String json = router.dispatch(method, path, ctx);
            ctx.writeJson(json);
        }
    }

    class StaticHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String reqPath = exchange.getRequestURI().getPath();
            if(reqPath.startsWith("/api")){
                // 防御: API交由 ApiHandler
                exchange.sendResponseHeaders(404, 0);
                exchange.getResponseBody().close();
                return;
            }
            Path file;
            if ("/ext/lwquery.js".equals(reqPath)) {
                file = Paths.get("d:/2022java/shixun/demo0801/WebRoot/js/lwquery.js");
            } else {
                file = webRoot.resolve(reqPath.substring(1));
                if(Files.isDirectory(file)){
                    file = webRoot.resolve("index.html");
                }
            }
            if(!Files.exists(file)){
                String notFound = "404 Not Found";
                exchange.sendResponseHeaders(404, notFound.length());
                try(OutputStream os = exchange.getResponseBody()){
                    os.write(notFound.getBytes());
                }
                return;
            }
            String mime = mimeType(file);
            byte[] bytes = Files.readAllBytes(file);
            exchange.getResponseHeaders().set("Content-Type", mime);
            exchange.sendResponseHeaders(200, bytes.length);
            try(OutputStream os = exchange.getResponseBody()){
                os.write(bytes);
            }
        }

        private String mimeType(Path p){
            String name = p.getFileName().toString().toLowerCase();
            if(name.endsWith(".html")) return "text/html; charset=utf-8";
            if(name.endsWith(".css")) return "text/css; charset=utf-8";
            if(name.endsWith(".js")) return "application/javascript; charset=utf-8";
            if(name.endsWith(".json")) return "application/json; charset=utf-8";
            return "text/plain; charset=utf-8";
        }
    }
}