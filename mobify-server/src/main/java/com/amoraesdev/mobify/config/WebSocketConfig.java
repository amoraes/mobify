package com.amoraesdev.mobify.config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.amoraesdev.mobify.web.WebsocketHandler;

/**
 * Websocket configuration class
 * @author Alessandro Moraes (alessandro at amoraesdev.com)
 */
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
	
	@Autowired
    private WebsocketHandler handler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
    	registry.addHandler(handler, "/ws/notifications").setAllowedOrigins("*");
        registry.addHandler(handler, "/sockjs/notifications").setAllowedOrigins("*").withSockJS();
    }
}