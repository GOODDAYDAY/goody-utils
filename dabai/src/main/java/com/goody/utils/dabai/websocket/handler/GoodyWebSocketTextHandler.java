package com.goody.utils.dabai.websocket.handler;

import com.goody.utils.dabai.websocket.config.GoodyWebSocketConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;

/**
 * websocket handler
 *
 * @author Goody
 * @version 1.0, 2022/11/29 16:29
 * @since 1.1.0
 */
@Service
@Slf4j
@RequiredArgsConstructor
@ConditionalOnBean(GoodyWebSocketConfig.class)
public class GoodyWebSocketTextHandler extends AbstractWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info("#afterConnectionEstablished session {}", session);
        // 3. connection success
    }

    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("#handleTextMessage session {}, message {}", session, message);
        // handle payload here
        // 4. receive payload
        session.sendMessage(new TextMessage(message.getPayload()));
    }

    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        log.info("#handlePongMessage session {}, message {}", session, message);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.info("#handleTransportError session {}", session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("#afterConnectionClosed session {} status {}", session, status);
        // 5. close session
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
