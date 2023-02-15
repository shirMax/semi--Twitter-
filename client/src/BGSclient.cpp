#include <stdlib.h>
#include <connectionHandler.h>
#include <thread>
#include <MsgEncoderDecoder.h>
#include <atomic>
#include <stdio.h>

/**
* This code assumes that the server replies the exact text the client sent it (as opposed to the practical session example)
*/

void t1(ConnectionHandler &connectionHandler, MsgEncoderDecoder &msgEncoderDecoder, std::atomic<bool> &stop,std::unique_lock<std::mutex> &lck,std::condition_variable &cv) {
    while (!stop) {
        const short bufSize = 1024;
        char buf[bufSize];
        std::cin.getline(buf, bufSize);
        std::string line(buf);
        int len = 0;
        char arr[bufSize];
        msgEncoderDecoder.encode(line, arr, len);
        if (!connectionHandler.sendBytes(arr, len)) {
            std::cout << "Disconnected. Exiting...\n" << std::endl;
            break;
        }
        if (line == "LOGOUT")
            cv.wait(lck);
    }
}

void t2(ConnectionHandler &connectionHandler, MsgEncoderDecoder &msgEncoderDecoder, std::atomic<bool> &stop,std::unique_lock<std::mutex> &lck,std::condition_variable &cv) {
    while (!stop) {
        std::string answer = "";
        char ch;
        while (answer == "") {
            if (!connectionHandler.getBytes(&ch, 1))
                stop = true;
            answer = msgEncoderDecoder.decodeNextByte(ch);
        }
        std::cout << answer << std::endl;
        if (answer == "ACK 3 ") {
            stop = true;
            cv.notify_all();
        }
        if (answer == "ERROR 3")
            cv.notify_all();
        answer = "";
    }
}

int main(int argc, char *argv[]) {
    if (argc < 3) {
        std::cerr << "Usage: " << argv[0] << " host port" << std::endl << std::endl;
        return -1;
    }
    std::string host = argv[1];
    short port = atoi(argv[2]);
    std::condition_variable cv;
    std::mutex mtx;
    std::unique_lock<std::mutex> lck(mtx);
    MsgEncoderDecoder msgEncoderDecoder;
    ConnectionHandler connectionHandler(host, port);
    if (!connectionHandler.connect()) {
        std::cerr << "Cannot connect to " << host << ":" << port << std::endl;
        return 1;
    }
    std::atomic<bool> stop(false);
    std::thread thread1(&t1, std::ref(connectionHandler), std::ref(msgEncoderDecoder), ref(stop),ref(lck),ref(cv));
    std::thread thread2(&t2, std::ref(connectionHandler), std::ref(msgEncoderDecoder), ref(stop),ref(lck),ref(cv));
    thread1.join();
    thread2.join();
    return 0;
}

