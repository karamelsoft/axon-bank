SHELL = /usr/bin/env sh
PROJECT = patients

clean:
	mvn clean

build:
	mvn compile test-compile -U

test:
	mvn test

install:
	mvn install -U

swarm-build:
	mvn --also-make clean install -U -DskipTests -Pdocker -Pswarm

swarm-deploy:
	docker stack deploy -c docker-compose.yml ${PROJECT}

swarm-deploy-all: swarm-deploy

swarm-destroy:
	docker stack rm ${PROJECT}

swarm-destroy-all: swarm-destroy

swarm-redeploy: swarm-destroy swarm-deploy

swarm-redeploy-all: swarm-destroy-all swarm-deploy-all

swarm-update: swarm-destroy swarm-build swarm-deploy

swarm-status:
	docker service ls | grep ${PROJECT}

