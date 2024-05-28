# MSA
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
![MSA project architecture drawio](https://github.com/s12171934/toy-gateway/assets/148848550/2d7fce01-df04-4640-b016-b623754bddf7)

## Gateway Diagram
![gateway drawio](https://github.com/s12171934/toy-gateway/assets/148848550/ed625f63-f438-4087-b9ff-e8a50383eb94)

## Auth Service API

### Spring Security Diagram
![Spring Security Auth Service API drawio](https://github.com/s12171934/toy-gateway/assets/148848550/b8b2b8ad-52f5-4b70-a889-d7130f4eb4e4)

### OAuth2 Diagram
![Auth Service OAuth2 drawio](https://github.com/s12171934/toy-gateway/assets/148848550/2c7b72ec-b8c7-427b-8eef-4992daa92a96)

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


