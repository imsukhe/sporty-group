# README - Jackpot Contribution and Reward Backend System

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

**1. BetController**
   1. POST /bets → Publishes a bet to Kafka
   1. GET /bets/{betId}/evaluate → Checks if the bet has won a jackpot


**2. BetService, BetServiceImpl** 
   1. Interface and implementation to handle bet publishing and evaluation
   2. Delegates to Kafka and strategy layers


**3. BetProcessorService**
   1. Called by Kafka consumer
   1. Applies contribution strategy and updates jackpot


**4. KafkaProducerService**
   1. Sends messages to Kafka 
   2. Generic KafkaTemplate<String, Object>


**5. KafkaConsumerService**
    1. Listens to Kafka topics
   2. Delegates to appropriate handler via registry

kafka/KafkaMessageHandler<T>

Interface for message handlers

kafka/KafkaHandlerRegistry

Registers handlers per topic

Singleton component used by consumer


BetRequest: input payload

Jackpot: jackpot config and state

JackpotContribution: contribution entry

JackpotReward: reward winner entry

EvaluationResponse: API response object

strategy/

ContributionStrategy, RewardStrategy: Strategy interfaces

ContributionStrategyFactory, RewardStrategyFactory: Factory classes for pluggable logic

db/InMemoryDb

Singleton in-memory store

Thread-safe maps for jackpots, contributions, rewards

Testing

BetServiceImplTest covers:

Kafka publishing

Evaluation logic for win/loss

Jackpot reset

Uses Mockito, JUnit 5, and static method mocking via mockito-inline

How to Run

Prerequisites

Java 17+

Maven 3.x

Docker + Docker Compose

Step 1: Start Kafka via Docker

Run Kafka:

docker-compose up -d
Step 2: Run Spring Boot Application

./mvnw spring-boot:run
API Usage

1. Publish a Bet
POST http://localhost:8080/bets
Content-Type: application/json

{
"betId": "bet101",
"userId": "user1",
"jackpotId": "jackpot1",
"amount": 100.0
}


2. Evaluate a Bet

GET http://localhost:8080/bets/bet101/evaluate


Notes

Jackpots must be added at runtime (e.g., via CommandLineRunner or internal utility)

Strategy logic (FIXED, VARIABLE) can be extended using factories

Kafka messages are deserialized using Spring's JsonDeserializer

