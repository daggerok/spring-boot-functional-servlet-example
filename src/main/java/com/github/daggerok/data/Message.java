package com.github.daggerok.data;

import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@RequiredArgsConstructor(staticName = "of")
public class Message {
  final UUID id;
  final LocalDateTime at;
  final String body;
}
