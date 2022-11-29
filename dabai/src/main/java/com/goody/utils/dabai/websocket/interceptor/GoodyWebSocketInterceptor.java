package com.goody.utils.dabai.websocket.interceptor;

import com.goody.utils.dabai.websocket.config.GoodyWebSocketConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * custom interceptor
 *
 * @author Goody
 * @version 1.0, 2022/11/29 16:05
 * @since 1.1.0
 */
@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnBean(GoodyWebSocketConfig.class)
public class GoodyWebSocketInterceptor implements HandshakeInterceptor {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        log.info("#beforeHandshake request {}", request);
        // authorize check here
        // 1. handshake
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        log.info("#afterHandshake request {}", request);
        // 2. handshake success
    }
}
