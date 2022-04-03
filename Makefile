.PHONY=clean

clean:
	mvn clean

build:
	mvn compile test-compile

test:
	mvn test

