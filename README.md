# Axon Bank

## Introduction

This project is a showcase project using: 

- Domain Driven Design
- Command and Query Responsibility Segregation
- Event Sourcing

implementing a distributed simplistic bank.

## Prerequisites

- JDK 17
- Maven
- Docker Desktop (Swarm Compliant)
- Make

## Install

### Containers Creation 
```bash
make container
```

### Deployment
```bash
make swarm-deploy
```

### Destroy
```bash
make swarm-destroy
```
