# README 
# Jackpot Contribution and Reward Backend System

## Overview

This project implements a backend system for processing bets, contributing to a jackpot pool, and evaluating rewards. It is built with Java Spring Boot and uses Kafka for message-driven communication. All data is stored in-memory using a Singleton pattern.

## Core Features

1. Publish bet via API and send to Kafka
2. Kafka consumer processes bet and applies contribution strategy
3. Evaluate if a bet wins the jackpot using reward strategy
4. In-memory database using thread-safe singleton
5. Strategy and Factory patterns for pluggable business logic
6. Unit tests with JUnit and Mockito

## Class Summary

### BetController
    - POST /bets → Publishes a bet to Kafka 
    - GET /bets/{betId}/evaluate → Checks if the bet has won a jackpot


### BetService, BetServiceImpl
    - Interface and implementation to handle bet publishing and evaluation
    - Delegates to Kafka and strategy layers


### BetProcessorService
    - Called by Kafka consumer
    - Applies contribution strategy and updates jackpot


### KafkaProducerService
    - Sends messages to Kafka 
    - Generic KafkaTemplate<String, Object>


### KafkaConsumerService
    - Listens to Kafka topics
    - Delegates to appropriate handler via registry

### KafkaMessageHandler
    Interface for message handlers

### KafkaHandlerRegistry
    - Registers handlers per topic
    - Singleton component used by consumer

### Models

    BetRequest: input payload
    
    Jackpot: jackpot config and state
    
    JackpotContribution: contribution entry
    
    JackpotReward: reward winner entry
    
    EvaluationResponse: API response object

### ContributionStrategy, RewardStrategy: Strategy interfaces

### ContributionStrategyFactory, RewardStrategyFactory: Factory classes for pluggable logic

## InMemoryDb
    - Singleton in-memory store
    - Thread-safe maps for jackpots, contributions, rewards

## Testing

### BetServiceImplTest covers:
    - Kafka publishing
    - Evaluation logic for win/loss
    - Jackpot reset
    - Uses Mockito, JUnit 5, and static method mocking via mockito-inline

## How to Run

### Prerequisites
    - Java 17+
    - Maven 3.x
    - Docker + Docker Compose

### Step 1: Start Kafka via Docker
    Run Kafka: docker-compose up -d

### Step 2: Run Spring Boot Application
    ./mvnw spring-boot:run

## API Usage

### 1. Create a Jackpot
    POST http://localhost:8080/jackpot
    Content-Type: application/json
    
    Request Body:
    {
    "id": "jackpot1",
    "initialAmount": 1000.0,
    "contributionType": "FIXED",
    "rewardType": "VARIABLE"
    }

### 2. Get a Specific Jackpot
    GET http://localhost:8080/jackpot/jackpot1

### 3. Get All Jackpots
    GET http://localhost:8080/jackpot

### 4. Publish a Bet
    POST http://localhost:8080/bet
    Content-Type: application/json

    Request Body:
    {
    "betId": "bet101",
    "userId": "user1",
    "jackpotId": "jackpot1",
    "amount": 100.0
    }

### 5. Evaluate a Bet
    GET http://localhost:8080/bet/evaluate/bet101

## Notes
    - Jackpots must be added at runtime (e.g., via CommandLineRunner or internal utility)
    - Strategy logic (FIXED, VARIABLE) can be extended using factories
    - Kafka messages are deserialized using Spring's JsonDeserializer


## Swagger / API Documentation

This project uses SpringDoc OpenAPI 3 to generate interactive Swagger UI documentation.

### URL: http://localhost:8080/swagger-ui/index.html
    - After starting the Spring Boot application, you can access all exposed endpoints (e.g. /bets, /bets/{betId}/evaluate) and their schemas from the browser.
    - You can test API inputs directly from Swagger without curl/Postman.
    - No additional config is needed — Swagger auto-discovers REST controllers and models.