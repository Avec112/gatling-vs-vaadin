# Gatling OSS
Gatling is a high-performance load testing tool built for efficiency, automation, and code-driven testing workflows.

Test scenarios are defined as code using an expressive DSL in Java, JavaScript, Scala, or Kotlin, making them easy to read, version, and maintain as part of your development workflow.

Gatling’s architecture is fully asynchronous. Virtual users are modeled as lightweight messages rather than threads, allowing you to simulate thousands of concurrent users with minimal system resources, ideal for modern, high-scale applications.

While Gatling offers robust support for HTTP out of the box, the load engine is protocol-agnostic. It also ships with JMS support and can be extended to handle other protocols.

## Documentation
https://docs.gatling.io/

## Where are my simulation(s)?
`src/test/java`

## How to run the simulation(s)?
`> mvn gatling:test`

## Requirements
- Java 21+
- Maven 3.9+
- 
## TODO
- Run our own web application
- Configure the simulation agains our own web application