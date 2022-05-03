.PHONY=clean

clean:
	mvnd clean

build:
	mvnd compile test-compile -T 4C

container:
	mvnd package -Pdocker

test:
	mvnd test

swarm-deploy:
	$(MAKE) -C infrastructure swarm-deploy
	$(MAKE) -C domains swarm-deploy
	$(MAKE) -C interfaces swarm-deploy
	$(MAKE) -C orchestrators swarm-deploy
	$(MAKE) -C views swarm-deploy

swarm-destroy:
	$(MAKE) -C infrastructure swarm-destroy
	$(MAKE) -C domains swarm-destroy
	$(MAKE) -C interfaces swarm-destroy
	$(MAKE) -C orchestrators swarm-destroy
	$(MAKE) -C views swarm-destroy

container-kill-apps:
	$(MAKE) -C domains container-kill
	$(MAKE) -C interfaces container-kill
	$(MAKE) -C orchestrators container-kill
	$(MAKE) -C views container-kill

container-kill-all: container-kill-apps
	$(MAKE) -C infrastructure container-kill
