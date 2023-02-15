CFLAGS:=-c -Wall -Weffc++ -g -std=c++11 -Iinclude
LDFLAGS:=-lboost_system -lboost_thread -pthread

all: BGSclient
	g++ -o bin/BGSclient bin/connectionHandler.o bin/BGSclient.o bin/MsgEncoderDecoder.o $(LDFLAGS)

BGSclient: bin/connectionHandler.o bin/BGSclient.o bin/MsgEncoderDecoder.o

bin/connectionHandler.o: src/connectionHandler.cpp
	g++ $(CFLAGS) -o bin/connectionHandler.o src/connectionHandler.cpp

bin/BGSclient.o: src/BGSclient.cpp
	g++ $(CFLAGS) -o bin/BGSclient.o src/BGSclient.cpp

bin/MsgEncoderDecoder.o: src/MsgEncoderDecoder.cpp
	g++ $(CFLAGS) -o bin/MsgEncoderDecoder.o src/MsgEncoderDecoder.cpp

.PHONY: clean
clean:
	rm -f bin/*