# Functional Servlet MVC [![Build Status](https://travis-ci.org/daggerok/spring-boot-functional-servlet-example.svg?branch=master)](https://travis-ci.org/daggerok/spring-boot-functional-servlet-example)
This is a later spring web (2.2.0.BUILD-SNAPSHOT) with functional servlet functionality!

## Key items

### Functional Servlet based Routes

```java
@Configuration
public class FunctionalRouter {

  @Bean
  public RouterFunction<ServerResponse> routes() {
    return RouterFunctions.route()
                          .POST("/fn", request -> ServerResponse.ok().body("Functional hello!"))
                          .GET("/**", request -> ServerResponse.ok().body("_self: " + request.path()))
                          .filter((request, next) -> {
                            var response = next.handle(request);
                            var headers = HttpHeaders.writableHttpHeaders(response.headers());
                            headers.add("X-FUNCTIONAL", "It's fucking awesome!");
                            return response;
                          })
                          .build();
  }
}
```

## Build, run and test

```bash
./mvnw                    # or: ./gradlew
java -jar ./target/*.jar  # or: ./build/libs/*.jar

http :8080/fn body=world
http :8080/mvc/
http :8080/mvc body=hello
http :8080/fn/
```

## zip sources (out of topic)

```bash
./gradlew sources # or ./mvnw package
```

links:

* [YouTube: Spring Tips: WebMvc.fn - the functional DSL for Spring MVC](https://www.youtube.com/watch?v=vZV2_9KVHJU)
* [See that useful article about zip with maven assembly](https://medium.com/@kasunpdh/using-the-maven-assembly-plugin-to-build-a-zip-distribution-5cbca2a3b052)
