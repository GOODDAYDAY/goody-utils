package com.goody.utils.dabai.websocket.config;

import com.goody.utils.dabai.websocket.handler.GoodyWebSocketTextHandler;
import com.goody.utils.dabai.websocket.interceptor.GoodyWebSocketInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.PostConstruct;

/**
 * websocket config
 *
 * @author Goody
 * @version 1.0, 2022/11/29 15:40
 * @since 1.0.0
 */
@Slf4j
@EnableWebSocket
@Configuration
@ConditionalOnProperty(value = "dabai.switch.websocket", havingValue = "true")
@RequiredArgsConstructor
public class GoodyWebSocketConfig implements WebSocketConfigurer {

    private final GoodyWebSocketTextHandler handler;
    private final GoodyWebSocketInterceptor interceptor;

    @PostConstruct
    public void init() {
        log.warn("GoodyWebSocketConfig start =============");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(handler, "console").setAllowedOrigins("*").addInterceptors(interceptor);
    }
}
