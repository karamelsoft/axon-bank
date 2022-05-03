.PHONY=clean

clean:
	mvn clean

build:
	mvn compile test-compile

container:
	mvn package -Pdocker

test:
	mvn test

swarm-deploy:
	$(MAKE) -C customer-dashboard swarm-deploy

swarm-destroy:
	$(MAKE) -C customer-dashboard swarm-destroy

swarm-redeploy: swarm-destroy swarm-deploy

swarm-update: swarm-destroy build swarm-deploy

container-kill:
	$(MAKE) -C customer-dashboard container-kill
