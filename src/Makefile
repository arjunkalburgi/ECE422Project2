# OS X
LDFLAGS1 = -I$(JAVA_HOME)/include -I$(JAVA_HOME)/INCLUDE/darwin -dynamiclib -fpic

# Linux
LDFLAGS2 = -I$(JAVA_HOME)/include -I$(JAVA_HOME)/INCLUDE/linux -shared -fpic

darwin:
	javac *.java
	javah SecurityFunctions
	gcc $(LDFLAGS1) -o encrypt.dylib encrypt.c
	gcc $(LDFLAGS1) -o decrypt.dylib decrypt.c

linux:
	javac *.java
	javah SecurityFunctions
	gcc $(LDFLAGS2) -o encrypt.dylib encrypt.c
	gcc $(LDFLAGS2) -o decrypt.dylib decrypt.c
