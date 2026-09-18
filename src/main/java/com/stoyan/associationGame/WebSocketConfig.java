package com.stoyan.associationGame;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private SimpleTextHandler simpleTextHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // Register the Spring-managed handler (the one the controller broadcasts through)
        // and accept any origin, mirroring the REST CORS setup. Phones reached the site over
        // a host/scheme that wasn't in the old allow-list and got a 403 on the handshake.
        registry.addHandler(simpleTextHandler, "/ws")
                .setAllowedOriginPatterns("*");
    }
}
