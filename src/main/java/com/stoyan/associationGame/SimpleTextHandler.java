package com.stoyan.associationGame;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SimpleTextHandler extends TextWebSocketHandler {

    private static final Map<Integer, Set<WebSocketSession>> sessions = new ConcurrentHashMap<>();

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload() == null ? "" : message.getPayload().trim();

        // Keep-alive used by the clients so mobile networks don't silently drop the connection.
        if ("ping".equalsIgnoreCase(payload)) {
            sendSafely(session, "pong");
            return;
        }

        final Integer gameId;
        try {
            gameId = Integer.valueOf(payload);
        } catch (NumberFormatException e) {
            // Anything that isn't a game id is ignored; throwing here would kill the session.
            System.out.println("Ignoring unexpected websocket payload: " + payload);
            return;
        }

        // A session may re-subscribe after a reconnect, so drop it from any previous game first.
        removeSession(session);
        sessions.computeIfAbsent(gameId, id -> ConcurrentHashMap.newKeySet()).add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        removeSession(session);
        System.out.println("Session closed: " + session.getId());
    }

    public void broadcast(int gameId, String msg) {
        Set<WebSocketSession> sessionSet = sessions.get(gameId);
        if (sessionSet == null || sessionSet.isEmpty()) {
            // Nobody is listening over the websocket - clients will pick this up by polling.
            return;
        }
        for (WebSocketSession session : sessionSet) {
            if (session.isOpen()) {
                sendSafely(session, msg);
            } else {
                sessionSet.remove(session);
            }
        }
    }

    public void removeGame(int gameId) {
        Set<WebSocketSession> sessionSet = sessions.remove(gameId);
        if (sessionSet == null) {
            return;
        }
        for (WebSocketSession session : sessionSet) {
            try {
                session.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void removeSession(WebSocketSession session) {
        for (Set<WebSocketSession> sessionSet : sessions.values()) {
            sessionSet.remove(session);
        }
    }

    /** One broken socket must not stop the broadcast to everybody else. */
    private void sendSafely(WebSocketSession session, String msg) {
        try {
            synchronized (session) {
                session.sendMessage(new TextMessage(msg));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
