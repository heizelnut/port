include .env

class:
	./tom clean
	./tom build

cli: class
	./tom run

serve: class
	./tom run serve

jar: class
	rm $(PKGNAME).jar
	./tom bundle
