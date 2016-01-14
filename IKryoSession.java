
import java.net.SocketAddress;

/**
 * Created by maotz on 2015-03-23.
 * 链接接口
 */
public interface IKryoSession {
    /**
     * 发送对象
     * @param _kryo_obj
     */
    void send(KryoObj _kryo_obj);

    /**
     * 取本地地址
     * @return 本地地址
     */
    SocketAddress getLocalAddress();

    /**
     * 取远端地址
     * @return 远程地址
     */
    SocketAddress getRemoteAddress();

    /**
     * 取关键字
     * @return  关键字
     */
    String getSessionKey();
}
