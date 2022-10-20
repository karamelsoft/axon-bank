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
	$(MAKE) -C accounts swarm-deploy
	$(MAKE) -C cards swarm-deploy
	$(MAKE) -C credits swarm-deploy
	$(MAKE) -C customers swarm-deploy

swarm-destroy:
	$(MAKE) -C accounts swarm-destroy
	$(MAKE) -C cards swarm-destroy
	$(MAKE) -C credits swarm-destroy
	$(MAKE) -C customers swarm-destroy

swarm-redeploy: swarm-destroy swarm-deploy

swarm-update: swarm-destroy build swarm-deploy

container-kill:
	$(MAKE) -C accounts container-kill
	$(MAKE) -C cards container-kill
	$(MAKE) -C credits container-kill
	$(MAKE) -C customers container-kill
