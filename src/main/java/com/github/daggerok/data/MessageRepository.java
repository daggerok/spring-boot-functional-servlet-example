package com.github.daggerok.data;

import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static java.time.LocalDateTime.now;

@Repository
public class MessageRepository {

  private final Map<UUID, Message> messages = new ConcurrentHashMap<>();

  public Message save(String body) {
    final UUID id = UUID.randomUUID();
    final Message message = Message.of(id, now(), body);
    messages.put(id, message);
    return message;
  }

  public Optional<Message> findById(UUID id) {
    return Optional.ofNullable(messages.getOrDefault(id, null));
  }

  public Collection<Message> findAll() {
    return messages.values().stream()
                   .sorted(Comparator.comparing(Message::getAt).reversed())
                   .collect(Collectors.toList());
  }
}
