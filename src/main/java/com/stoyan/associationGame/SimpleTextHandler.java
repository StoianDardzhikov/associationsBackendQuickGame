package com.stoyan.associationGame;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

@Component
public class SimpleTextHandler extends TextWebSocketHandler {

    private static final Map<Integer, Set<WebSocketSession>> sessions = Collections.synchronizedMap(new HashMap<>());

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Integer gameId = Integer.parseInt(message.getPayload());
        Set<WebSocketSession> sessionSet = sessions.get(gameId);
        if (sessionSet == null) {
            sessionSet = new HashSet<>();
        }
        sessionSet.add(session);
        sessions.put(gameId, sessionSet);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, org.springframework.web.socket.CloseStatus status) throws Exception {
        for (Set<WebSocketSession> sessionSet : sessions.values()) {
            sessionSet.remove(session);
        }
        System.out.println("Session closed: " + session.getId());
    }

    public void broadcast(int gameId, String msg) throws Exception {
        synchronized (sessions) {
            Set<WebSocketSession> sessionSet = sessions.get(gameId);
            for (WebSocketSession session : sessionSet) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(msg));
                }
            }
        }
    }

    public void removeGame(int gameId) {
        synchronized (sessions) {
            Set<WebSocketSession> sessionSet = sessions.remove(gameId);
            try {
                for (WebSocketSession session : sessionSet) {
                    session.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
