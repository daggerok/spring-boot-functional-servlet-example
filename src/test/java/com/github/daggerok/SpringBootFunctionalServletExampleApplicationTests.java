package com.github.daggerok;

import com.github.daggerok.data.Message;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@Log4j2
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class SpringBootFunctionalServletExampleApplicationTests {

  @LocalServerPort
  Integer port;

  @Autowired
  TestRestTemplate client;

  private String baseUrl() {
    return format("http://127.0.0.1:%s", port);
  }

  @Test
  public void test() {
    // given no messages available on functional resource:
    var types = new ParameterizedTypeReference<List<Message>>() {};
    var entity = client.exchange(baseUrl() + "/fn", HttpMethod.GET, null, types);
    var emptyBody = entity.getBody();
    log.info("emptyBody: {}", emptyBody);
    assertThat(emptyBody).isEmpty();

    // when message has been created on functional resource:
    var type = new ParameterizedTypeReference<Void>() {};
    var request = new HttpEntity<>(Message.of(null, null, "Hello!"));
    var response = client.exchange(baseUrl() + "/fn", HttpMethod.POST, request, type);
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    // and location to created item was produced back:
    var location = response.getHeaders().getLocation();
    log.info("location: {}", location);
    assertThat(location).isNotNull();
    assertThat(location.getScheme()).isEqualTo("http");
    assertThat(location.getHost()).isEqualTo("127.0.0.1");
    assertThat(location.getPort()).isEqualTo(port);
    assertThat(location.getPath()).containsIgnoringCase("/fn/");

    // then it should be available and queried from functional resource:
    var messageResponse = client.exchange(location, HttpMethod.GET, null, Message.class);
    var message = messageResponse.getBody();
    log.info("message: {}", message);
    assertThat(message).isNotNull();
    assertThat(message.getBody()).isNotNull()
                                 .containsIgnoringCase("hello");
  }
}
