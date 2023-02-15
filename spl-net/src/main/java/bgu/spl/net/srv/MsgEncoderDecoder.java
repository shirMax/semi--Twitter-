package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.srv.Messages.ClientToServer.*;
import bgu.spl.net.srv.Messages.Message;
import bgu.spl.net.srv.Messages.SeverToClient.*;
import bgu.spl.net.srv.Messages.SeverToClient.Notification;
import bgu.spl.net.srv.Messages.SeverToClient.Error;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MsgEncoderDecoder implements MessageEncoderDecoder<Message> {
    private byte[] bytes = new byte[1 << 10];
    int len = 0;

    @Override
    public Message decodeNextByte(byte nextByte) {
        if (nextByte == ';') {
            String[] values;
            Message msg = null;
            switch (bytesToShort(bytes)) {
                case 1: {
                    values = new String[3];
                    getStrings(values);
                    msg = new Register(values[0], values[1], values[2]);
                    break;
                }
                case 2: {
                    values = new String[2];
                    getStrings(values);
                    msg = new LogIn(values[0], values[1], bytes[len - 1] == 1);
                    break;
                }
                case 3: {
                    msg = new LogOut();
                    break;
                }
                case 4: {
                    msg = new FollowUnfollow(bytes[2] == 1, new String(bytes, 3, len - 3));
                    break;
                }
                case 5: {
                    msg = new Post(new String(bytes, 2, len - 2));
                    break;
                }
                case 6: {
                    values = new String[3];
                    getStrings(values);
                    msg = new PM(values[0], values[1], values[2]);
                    break;
                }
                case 7: {
                    msg = new LogStat();
                    break;
                }
                case 8: {
                    msg = new Stat(Arrays.asList(new String(bytes, 2, len - 2).split("\\|")));
                    break;
                }
                case 12: {
                    msg = new Block(new String(bytes, 2, len - 2));
                    break;
                }
            }
            len = 0;
            bytes = new byte[1 << 10];
            return msg;
        } else

            pushByte(nextByte);
        return null;
    }

    private void pushByte(byte nextByte) {
        if (len >= bytes.length)
            bytes = Arrays.copyOf(bytes, len * 2);
        bytes[len] = nextByte;
        len++;
    }

    @Override
    public byte[] encode(Message message) {
        byte[] msg = null;
        byte[] opCode = shortToBytes(message.getOpCode());
        switch (message.getOpCode()) {
            case 9: {
                byte notificationType = (byte) (((Notification) message).isNotificationType() ? 1 : 0);
                byte[] postingUser = ((Notification) message).getPostingUser().getBytes();
                byte[] content = ((Notification) message).getContent().getBytes();
                msg = ByteBuffer.allocate(opCode.length + 1 + 1 + postingUser.length + 1 + content.length + 1)
                        .put(opCode)
                        .put(notificationType)
                        .put(postingUser).put((byte) 0)
                        .put(content).put((byte) 0)
                        .put(";".getBytes()).array();
                break;
            }
            case 10: {
                short msgOpCode = ((Ack) message).getMessageOpCode();
                byte[] optional = ((Ack) message).getOptional().getBytes();
                if (((Ack) message).getMessageOpCode() == 7 || ((Ack) message).getMessageOpCode() == 8)
                    msg = ByteBuffer.allocate(13)
                            .put(opCode)
                            .put(shortToBytes(msgOpCode))
                            .put(shortToBytes(Short.parseShort(((Ack) message).getOptional().split(" ")[0])))
                            .put(shortToBytes(Short.parseShort(((Ack) message).getOptional().split(" ")[1])))
                            .put(shortToBytes(Short.parseShort(((Ack) message).getOptional().split(" ")[2])))
                            .put(shortToBytes(Short.parseShort(((Ack) message).getOptional().split(" ")[3])))
                            .put(";".getBytes()).array();
                else
                    msg = ByteBuffer.allocate(optional.length + 5)
                            .put(opCode)
                            .put(shortToBytes(msgOpCode))
                            .put(optional)
                            .put(";".getBytes()).array();
                break;
            }
            case 11: {
                byte[] msgOpCode = shortToBytes(((Error) message).getMessageOpcode());
                msg = ByteBuffer.allocate(5)
                        .put(opCode)
                        .put(msgOpCode)
                        .put(";".getBytes()).array();
                break;
            }
        }
        return msg;
    }

    private short bytesToShort(byte[] byteArr) {
        short result = (short) ((byteArr[0] & 0xff) << 8);
        result += (short) (byteArr[1] & 0xff);
        return result;
    }

    public byte[] shortToBytes(short num) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte) ((num >> 8) & 0xFF);
        bytesArr[1] = (byte) (num & 0xFF);
        return bytesArr;
    }

    private void getStrings(String[] values) {
        int offset = 2;
        int length;
        for (int i = 0; i < values.length; i++) {
            length = 0;
            while (bytes[offset + length] != 0)
                length++;
            //        length--;
            values[i] = new String(bytes, offset, length++, StandardCharsets.UTF_8);
            offset = offset + length;
        }
    }

}
