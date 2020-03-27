# Axon Demo

## Introduction

This project goal is to show a simple but not Hello World simple way of working with Axon.

## Philosophy
* Mono repo (This won't change)
* Micro-services
    * One container for the Write Model
    * Many containers (1 by default) for the Read model
    * One container for the interface of communication (Json/HTTP)

## Technologies
* Maven (Gradle will come soom)
* Kotlin (Because... What else?)
* Docker and Swarm (Simple I said)
* Makefile (Yeah! Makefile! Because when this will be migrated to Gradle nothing will change! Boom! O_O)

## Usage
### Getting the code
```bash
$ git clone git@github.com:karamelsoft/axon-demo.git
```

### Using the Makefile

```bash
$ make swarm-init           # create a swarm
$ make swarm-network        # create the network
$ make swarm-build          # build with swarm dependencies and configuration
$ make swarm-deploy         # deploy to swarm
$ make swarm-destroy        # remove from swarm

# other stuff have a look inside ;-)
```

