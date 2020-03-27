PROJECT = infra

clean:
	mvn clean

build:
	mvn compile test-compile -U

test:
	mvn test

install:
	mvn install -U -Pdocker

swarm-init:
	docker swarm init

swarm-network:
	docker network create -d overlay --attachable karamelsoft

swarm-secrets:
	echo "postgres" | docker secret create postgres_password -

swarm-build:
	mvn clean install -U -Pdocker

swarm-deploy:
	docker stack deploy -c docker-compose.yml ${PROJECT}

swarm-destroy:
	@echo "======================================================================"
	@echo "swarm destroy infrastructure"
	@echo "======================================================================"
	@echo
	docker stack rm ${PROJECT}

swarm-redeploy: swarm-destroy swarm-deploy

swarm-update: swarm-destroy swarm-build swarm-deploy

swarm-status:
	docker service ls | grep ${PROJECT}
