.PHONY=clean
STACK=account-registration

clean:
	mvn clean

build:
	mvn compile test-compile

container:
	mvn package -Pdocker

test:
	mvn test

swarm-deploy:
	docker stack deploy -c docker-compose.yml $(STACK)

swarm-destroy:
	docker stack rm $(STACK)

container-kill:
	docker ps | grep $(STACK) | awk '{ print $$1 }' | xargs -I {} docker rm {} --force
