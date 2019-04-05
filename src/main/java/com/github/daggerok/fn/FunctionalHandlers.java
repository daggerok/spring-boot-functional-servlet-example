package com.github.daggerok.fn;

import com.github.daggerok.data.Message;
import com.github.daggerok.data.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import java.net.URI;
import java.util.UUID;

import static java.lang.String.format;

@Component
@RequiredArgsConstructor
public class FunctionalHandlers {

  final MessageRepository messages;

  public ServerResponse handleSave(ServerRequest request) throws Exception {
    var message = request.body(Message.class);
    var uuid = messages.save(message.getBody()).getId();
    var uri = request.uri();
    var scheme = uri.getScheme();
    var authority = uri.getAuthority();
    var path = format("%s://%s/fn/%s", scheme, authority, uuid);
    return ServerResponse.created(URI.create(path)).build();
  }

  public ServerResponse handleGetOne(ServerRequest request) {
    var uuid = request.pathVariable("uuid");
    return ServerResponse.ok()
                         .contentType(MediaType.APPLICATION_JSON_UTF8)
                         .body(messages.findById(UUID.fromString(uuid)));
  }

  public ServerResponse handleGetAll(ServerRequest request) {
    return ServerResponse.ok()
                         .contentType(MediaType.APPLICATION_JSON_UTF8)
                         .body(messages.findAll());
  }
}
