package com.github.daggerok.mvc;

import com.github.daggerok.data.Message;
import com.github.daggerok.data.MessageRepository;
import io.undertow.servlet.spec.HttpServletRequestImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.Collection;
import java.util.UUID;

import static java.lang.String.format;
import static org.springframework.http.ResponseEntity.created;
import static org.springframework.http.ResponseEntity.notFound;

@RestController
@RequiredArgsConstructor
public class MvcResource {

  final MessageRepository messages;

  @GetMapping("/mvc/{uuid}")
  public ResponseEntity<Message> getOne(@PathVariable("uuid") UUID uuid) {
    return messages.findById(uuid)
                   .map(ResponseEntity::ok)
                   .orElse(notFound().build());
  }

  @GetMapping("/mvc/**")
  public Collection<Message> getAll() {
    return messages.findAll();
  }

  @PostMapping("/mvc/**")
  public ResponseEntity post(@RequestBody Message message, HttpServletRequest request) {
    var req = (HttpServletRequestImpl) request;
    var scheme = req.getScheme();
    var hostAndPorts = req.getExchange().getHostAndPort();
    var uuid = messages.save(message.getBody()).getId();
    var path = format("%s://%s/mvc/%s", scheme, hostAndPorts, uuid);
    return created(URI.create(path)).build();
  }
}
