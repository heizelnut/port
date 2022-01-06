include .env

class:
	./tom clean
	./tom build

cli: class
	./tom run

jar: class
	rm $(PKGNAME).jar
	./tom bundle

serve: jar
	java -jar $(PKGNAME).jar serve
