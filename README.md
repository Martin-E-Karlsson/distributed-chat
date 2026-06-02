# Distributed Chat

A distributed chat application built using independent microservices.

## Architecture
- **bff** – Backend-for-Frontend: REST API for clients, validates JWT, routes to internal services
- **auth-service** – issues JWT on login
- **user-service** – user CRUD, own database, gRPC endpoint for profile lookups
- **message-service** – stores messages, publishes `message-published` to the message queue
- **bot-service** (optional) – consumes `message-published`, posts automatic replies

## Tech
- Java 26, Spring Boot 4.0.6, Maven (per service)
- gRPC for internal calls, RabbitMQ as event bus, JWT auth
- Each service is an independent project; orchestrated with Docker Compose (later)

## Status
Work in progress — built test-first (TDD), one service at a time.