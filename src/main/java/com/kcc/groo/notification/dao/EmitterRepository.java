package com.kcc.groo.notification.dao;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import org.springframework.stereotype.Repository;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Repository
public class EmitterRepository {

    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public void save(String userId, SseEmitter emitter) {
        emitters.put(userId, emitter);
    }

    public SseEmitter get(String userId) {
        return emitters.get(userId);
    }

    public void delete(String userId) {
        emitters.remove(userId);
    }
}
