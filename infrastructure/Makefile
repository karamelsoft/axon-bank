NETWORK=axon-demo
STACK=infra

build:
	$(MAKE) -C reverseproxy build

swarm-setup: swarm-init swarm-network

swarm-init:
	-docker swarm init

swarm-network:
	-docker network create -d overlay --attachable $(NETWORK)

swarm-deploy: swarm-setup
	docker stack deploy -c docker-compose.yml $(STACK)

swarm-destroy:
	docker stack rm $(STACK)

swarm-redeploy: swarm-destroy swarm-deploy

swarm-update: swarm-destroy build swarm-deploy

swarm-status:
	docker service ls | grep $(STACK)

container-kill:
	docker ps | grep $(STACK) | awk '{ print $$1 }' | xargs -I {} docker rm {} --force
