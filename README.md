# Distributed API Rate Limiter using Spring Boot, Redis, Docker, and Nginx

A production-grade distributed rate limiter built with Spring Boot,
Redis, Docker, and Nginx, implementing the Sliding Window algorithm
using Redis Lua scripting to ensure atomic, accurate, and scalable rate
limiting across multiple application instances.

This project demonstrates real-world backend infrastructure patterns
used in high-traffic systems such as Stripe, GitHub, and Cloudflare.

------------------------------------------------------------------------

# Problem Statement

Modern APIs must defend against:

-   Brute-force login attacks
-   DDoS traffic spikes
-   API abuse and unfair usage
-   Resource exhaustion

In distributed deployments with multiple server instances, rate limiting
must be:

-   Centralized
-   Consistent across all instances
-   Atomic under concurrency
-   Horizontally scalable

This project solves this using Redis as a centralized distributed state
store.

------------------------------------------------------------------------

# System Architecture

                ┌─────────────┐
                │   Client    │
                └──────┬──────┘
                       │ HTTP Request
                       ▼
                ┌─────────────┐
                │    Nginx    │
                │ LoadBalancer│
                └──────┬──────┘
                       │
        ┌──────────────┼──────────────┐
        ▼              ▼              ▼
┌─────────────┐ ┌─────────────┐ ┌─────────────┐
│ Spring Boot │ │ Spring Boot │ │ Spring Boot │
│ Instance 1  │ │ Instance 2  │ │ Instance 3  │
└──────┬──────┘ └──────┬──────┘ └──────┬──────┘
       │               │               │
       └───────────────┬───────────────┘
                       ▼
                ┌─────────────┐
                │    Redis    │
                │ Distributed │
                │ State Store │
                └─────────────┘

------------------------------------------------------------------------

# Core Features

## Distributed Rate Limiting

All application instances share a centralized Redis datastore, ensuring
consistent enforcement regardless of which instance handles the request.

## Sliding Window Rate Limiting Algorithm

Implemented using Redis Sorted Sets.

Advantages:

-   Accurate rolling window
-   Prevents boundary burst problem
-   Production-grade accuracy

Redis operations used:

-   ZADD
-   ZREMRANGEBYSCORE
-   ZCARD

## Atomic Execution using Redis Lua Script

Lua scripts ensure:

-   Atomic rate-limit checks
-   No race conditions
-   Safe under high concurrency

## Load Balancing using Nginx

Nginx distributes incoming traffic across multiple Spring Boot instances
using round-robin strategy.

## Per-User and Per-Endpoint Rate Limiting

Rate limit keys follow structure:

rl:{client_ip}:{endpoint}

Example:

rl:127.0.0.1:/api/data

## Standard Rate Limit Headers

Responses include:

-   X-RateLimit-Limit
-   X-RateLimit-Remaining
-   X-RateLimit-Reset
-   Retry-After

------------------------------------------------------------------------

# Tech Stack

Backend:

-   Java 25
-   Spring Boot
-   Spring Web
-   Spring Data Redis

Infrastructure:

-   Redis
-   Docker
-   Docker Compose
-   Nginx

Concurrency Control:

-   Redis Lua scripting

------------------------------------------------------------------------

# Project Structure

ratelimiter/
│
├── controller/
│   └── DataController.java
│
├── service/
│   ├── SlidingWindowRateLimiter.java
│   └── FixedWindowRateLimiter.java
│
├── filter/
│   └── RateLimiterFilter.java
│
├── redis/
│   └── SlidingWindowLuaExecutor.java
│
├── config/
│   └── RedisConfig.java
│
├── dto/
│   └── RateLimitDecision.java
│
├── nginx.conf
├── Dockerfile
├── docker-compose.yml
├── pom.xml
└── README.md

------------------------------------------------------------------------

# Running the Project

## Prerequisites

-   Docker
-   Docker Compose

## Build and Run

docker-compose up --build

This starts:

-   Redis
-   3 Spring Boot instances
-   Nginx load balancer

## Access API

http://localhost/api/data

------------------------------------------------------------------------

# Testing Rate Limiting

Send multiple requests:

curl http://localhost/api/data

When limit exceeded:

HTTP 429 Too Many Requests

------------------------------------------------------------------------

# Verify Distributed Behavior

Check logs:

docker logs app1\
docker logs app2\
docker logs app3

You will observe requests distributed across instances.

Redis ensures consistent rate limiting globally.

------------------------------------------------------------------------

# Engineering Concepts Demonstrated

-   Distributed Systems Architecture
-   Stateless Service Design
-   Load Balancing
-   Redis Data Structures
-   Atomic Operations using Lua
-   Sliding Window Rate Limiting Algorithm
-   Docker Containerization
-   Horizontal Scalability

------------------------------------------------------------------------

# Future Improvements

-   Token Bucket algorithm
-   Per-tier rate limits (Free vs Premium users)
-   Redis Cluster support
-   Kubernetes deployment
-   Monitoring using Prometheus and Grafana

------------------------------------------------------------------------

# Author

Saajid Ahamed

GitHub: https://github.com/SaajidAhamed007/RateLimiter
