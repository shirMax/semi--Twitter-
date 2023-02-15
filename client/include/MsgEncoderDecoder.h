//
// Created by shirm on 02/01/2022.
//

#ifndef BOOST_ECHO_CLIENT_MSGENCODERDECODER_H
#define BOOST_ECHO_CLIENT_MSGENCODERDECODER_H


#include <vector>

class MsgEncoderDecoder {
public:
    MsgEncoderDecoder();

    void pushByte(char nextByte);

    std::string decodeNextByte(char nextByte);

    void encode(std::string, char arr[], int &len);

    void shortToBytes(short, char[]);

//    void shortToBytes(short num, std::vector<char> &bytesArr);

    short bytesToShort(char bytesArr[]);

    std::vector<std::string> getStrings(int size, int offset, char bytes[]);

//    virtual ~MsgEncoderDecoder();
//    MsgEncoderDecoder(MsgEncoderDecoder &&other);
//    MsgEncoderDecoder(const MsgEncoderDecoder &other);
//    MsgEncoderDecoder& operator=(const MsgEncoderDecoder &other);
//    MsgEncoderDecoder& operator=(MsgEncoderDecoder && other);
private:
    std::vector<char> cBytes;

    std::vector<std::string> split(std::string);

    void stringToBytes(std::vector<std::string>, char bytes[], int &);

//    void stringToBytes(std::vector<std::string> segList, std::vector<char> toServer, int &len);
};


#endif //BOOST_ECHO_CLIENT_MSGENCODERDECODER_H
