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
	$(MAKE) -C account-registration swarm-deploy
	$(MAKE) -C customer-registration swarm-deploy
	$(MAKE) -C payment swarm-deploy

swarm-destroy:
	$(MAKE) -C account-registration swarm-destroy
	$(MAKE) -C customer-registration swarm-destroy
	$(MAKE) -C payment swarm-destroy

container-kill:
	$(MAKE) -C account-registration container-kill
	$(MAKE) -C customer-registration container-kill
	$(MAKE) -C payment container-kill
