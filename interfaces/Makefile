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
	$(MAKE) -C interfaces-http swarm-deploy

swarm-destroy:
	$(MAKE) -C interfaces-http swarm-destroy

swarm-redeploy: swarm-destroy swarm-deploy

container-kill:
	$(MAKE) -C interfaces-http container-kill
