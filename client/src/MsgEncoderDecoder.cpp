//
// Created by shirm on 02/01/2022.
//

#include <iosfwd>
#include <iostream>
#include "../include/MsgEncoderDecoder.h"
#include <vector>
#include <sstream>
#include <chrono>
#include <ctime>
#include <map>
MsgEncoderDecoder::MsgEncoderDecoder():cBytes(){
}

void MsgEncoderDecoder::encode(std::string input, char toServer[], int &len) {
    std::string opCom = input.substr(0, input.find(' '));
    input = input.substr(input.find(' ') + 1);
    std::map<std::string, short> my_map = {
            {"REGISTER", 1},
            {"LOGIN",    2},
            {"LOGOUT",   3},
            {"FOLLOW",   4},
            {"POST",     5},
            {"PM",       6},
            {"LOGSTAT",  7},
            {"STAT",     8},
            {"BLOCK",    12},
    };
    short opCode = my_map[opCom];
    switch (opCode) {
        case 1: {
            std::vector<std::string> segList = split(input);
            shortToBytes(opCode, toServer);
            stringToBytes(segList, toServer, len);
            break;
        }
        case 2: {
            std::vector<std::string> seglist = split(input);
            shortToBytes(opCode, toServer);
            stringToBytes(seglist, toServer, len);
            toServer[len - 2] = toServer[len - 2] == 48 ? 0 : 1;
            break;
        }
        case 3: {
            shortToBytes(opCode, toServer);
            len = 3;
            break;
        }
        case 4: {
            shortToBytes(opCode, toServer);
            int i = 2;
            toServer[i++] = input.substr(0, 1) == "1" ? 1 : 0;
            for (char c: input.substr(2)) {
                toServer[i++] = c;
            }
            len = i + 1;
            break;
        }
        case 5: {
            shortToBytes(opCode, toServer);
            std::vector<std::string> seglist;
            seglist.push_back(input);
            stringToBytes(seglist, toServer, len);
            break;
        }
        case 6: {
            std::vector<std::string> segList;
            segList.push_back(input.substr(0, input.find(' ')));
            segList.push_back(input.substr(input.find(' ') + 1));
            time_t now = time(0);
            tm *ltm = localtime(&now);
            segList.push_back(
                    std::to_string(ltm->tm_mday) + "-" +
                    std::to_string(1 + ltm->tm_mon) + "-" +
                    std::to_string(1900 + ltm->tm_year));
            shortToBytes(opCode, toServer);
            stringToBytes(segList, toServer, len);
            break;
        }
        case 7: {
            shortToBytes(opCode, toServer);
            len = 3;
            break;
        }
        case 8: {
            shortToBytes(opCode, toServer);
            std::vector<std::string> seglist;
            seglist.push_back(input);
            stringToBytes(seglist, toServer, len);
            break;
        }
        case 12: {
            shortToBytes(opCode, toServer);
            std::vector<std::string> seglist;
            seglist.push_back(input);
            stringToBytes(seglist, toServer, len);
            break;
        }
        default: {
            break;
        }
    }
    toServer[len - 1] = ';';
}

std::string MsgEncoderDecoder::decodeNextByte(char nextByte) {
    if (nextByte == ';') {
        char *bytes = &cBytes[0];
        short opCode = bytesToShort(bytes);
        std::string message;
        switch (opCode) {
            case 9 : {
                std::string pmPublic = bytes[2] ? "PUBLIC" : "PM";
                std::vector<std::string> values = getStrings(2, 3, bytes);
                message = "NOTIFICATION " + pmPublic + " " + values[0] + " " + values[1];
                break;
            }
            case 10 : {
                char msgOpCode[2]={bytes[2], bytes[3]};
                short msgOpCodeShort = bytesToShort(msgOpCode);
                std::string optional;
                if (msgOpCodeShort == 7 || msgOpCodeShort == 8) {
                    char age[2] = {bytes[4], bytes[5]};
                    char numPosts[2] = {bytes[6], bytes[7]};
                    char numFollowers[2] = {bytes[8], bytes[9]};
                    char numFollowing[2] = {bytes[10], bytes[11]};
                    optional =
                            std::to_string(bytesToShort(age)) + " " +
                            std::to_string(bytesToShort(numPosts)) + " " +
                            std::to_string(bytesToShort(numFollowers)) + " " +
                            std::to_string(bytesToShort(numFollowing));
                } else
                    optional = cBytes.size() > 4 ? std::string(cBytes.begin() + 4, cBytes.end()) : "";
                message = "ACK " + std::to_string(msgOpCodeShort) + " " + optional;
                break;
            }
            case 11: {
                char msgOpCode[2] = {bytes[2], bytes[3]};
                short msgOpCodeShort = bytesToShort(msgOpCode);
                message = "ERROR " + std::to_string(msgOpCodeShort);
                break;
            }
            default: {
                break;
            }
        }
        cBytes.clear();
        // delete bytes;
        return message;
    }
    pushByte(nextByte);
    return "";
}

void MsgEncoderDecoder::pushByte(char nextByte) {
    cBytes.push_back(nextByte);
}

void MsgEncoderDecoder::stringToBytes(std::vector<std::string> segList, char toServer[], int &len) {
    int i = 2;
    for (const std::string &val: segList) {
        for (char c: val)
            toServer[i++] = c;
        toServer[i++] = 0;
    }
    len = i;
}

std::vector<std::string> MsgEncoderDecoder::split(std::string input) {
    std::stringstream in(input);
    std::string segment;
    std::vector<std::string> seglist;
    while (std::getline(in, segment, ' ')) {
        seglist.push_back(segment);
    }
    return seglist;
}

void MsgEncoderDecoder::shortToBytes(short num, char bytesArr[]) {
    bytesArr[0] = ((num >> 8) & 0xFF);
    bytesArr[1] = (num & 0xFF);
}

short MsgEncoderDecoder::bytesToShort(char bytesArr[]) {
    short result = (short) ((bytesArr[0] & 0xff) << 8);
    result += (short) (bytesArr[1] & 0xff);
//    delete bytesArr;
    return result;
}

std::vector<std::string> MsgEncoderDecoder::getStrings(int size, int offset, char bytes[]) {
    std::vector<std::string> values(size);
    int length;
    for (auto &value: values) {
        length = 0;
        while (bytes[offset + length] != 0)
            length++;
        value = std::string(bytes + offset, length++);
        offset += length;
    }
    return values;
}
