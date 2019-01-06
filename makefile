all: 
	
	javac -cp .:./commons-net-3.3.jar client.java 
	java -cp .:./commons-net-3.3.jar client

clean: 
	rm *.class


#/bin/sh: 1: .: client: not found
#/bin/sh: 1: client: not found