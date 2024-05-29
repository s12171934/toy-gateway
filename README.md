![MSA project architecture drawio](https://github.com/s12171934/toy-gateway/assets/148848550/e921ab97-0160-499f-b793-41d688e7f899)# MSA
### All repository is Here
https://github.com/stars/s12171934/lists/msa-project

## Used Version
Java 21   
Spring Boot 3.2.6   
Spring Dependency Management 1.1.5   
Spring Cloud Version 2023.0.1   
JJWT 0.12.3   
Node.js 20.13.1   
Express 4.19.2   
Eureka-js-clientL 4.5.0   


## Architecture
![MSA project architecture drawio](https://github.com/s12171934/toy-gateway/assets/148848550/0b6f1f8b-303c-4d92-aeef-b1b7f3264d43)


## Gateway Diagram
![gateway drawio](https://github.com/s12171934/toy-gateway/assets/148848550/5b0cb993-e6ee-4823-b3c0-3057269d8eb9)

## Auth Service API

### Spring Security Diagram
![Spring Security Auth Service API drawio](https://github.com/s12171934/toy-gateway/assets/148848550/b3101bcc-94fa-4467-9eaa-925319c8a788)


### OAuth2 Diagram
![Auth Service OAuth2 drawio](https://github.com/s12171934/toy-gateway/assets/148848550/a85c2209-94c0-4c40-9968-30640d196ce5)


### Gateway Application.yml
```
server:
  port: [PORT]

spring:
  application:
    name: gateway

  cloud:
    gateway:
      routes:
        - id: auth
          uri: lb://AUTH
          predicates:
            - Path=/auth/**

        # OAuth2 code Redirect Uri
        - id: auth
          uri: lb://AUTH
          predicates:
            - Path=/auth/**

        # OAuth2 Login Redirect Uri
        - id: auth
          uri: lb://AUTH
          predicates:
            - Path=/login/**

        - id: user
          uri: lb://USER-SERVICE
          predicates:
            - Path=/user/**

        - id: board
          uri: lb://BOARD-SERVICE
          predicates:
            - Path=/board/**    

eureka:
  client:
    register-with-eureka: true
    fetch-registry: true
    service-url:
      defaultZone: [EUREKA_SERVER_URL]

jwt:
  secret: [SECRET KEY]

```


