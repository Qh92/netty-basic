package netty.protocoltcp;

/**
 * 协议包
 *
 * @author Qh
 * @version 1.0
 * @date 2021/11/18 17:30
 */
public class MessageProtocol {

    /** 协议包的长度 */
    private int length;

    private byte[] content;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }
}
