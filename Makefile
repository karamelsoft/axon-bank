SHELL = /usr/bin/env sh

clean:
	mvn clean

build:
	mvn compile test-compile -U

test:
	mvn test

install:
	mvn install

deploy:
	mvn deploy

feature-start:
	mvn gitflow:feature-start

feature-finish:
	mvn gitflow:feature-finish

release:
	mvn gitflow:release

hotfix-start:
	mvn gitflow:hotfix-start

hotfix-start-auto:
	mvn -B gitflow:hotfix-start

hotfix-finish:
	mvn gitflow:hotfix-finish

swarm-build:
	mvn clean install -DskipTests -Pdocker -Pswarm
