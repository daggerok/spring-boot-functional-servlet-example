package com.github.daggerok.fn;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.web.servlet.function.RouterFunctions.route;

@Log4j2
@Configuration
public class FunctionalRoute {

  @Bean
  public RouterFunction<ServerResponse> routes(FunctionalHandlers handlers) {
    return route().POST("/fn/**", handlers::handleSave)
                  .GET("/fn/{uuid}", handlers::handleGetOne)
                  .GET("/fn/**", handlers::handleGetAll)
                  .filter((request, next) -> {
                    log.info("Functional filter started!");
                    var response = next.handle(request);
                    var headers = HttpHeaders.writableHttpHeaders(response.headers());
                    headers.add("X-FUNCTIONAL", "It's fucking awesome!");
                    log.info("On Functional filter exiting...");
                    return response;
                  })
                  .build();
  }
}
