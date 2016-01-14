
import com.etrans.lib.net.tcp.ITcpSession;

import java.net.SocketAddress;

/**
 * Created by maotz on 2015-03-20.
 * 链接对象
 */
class KryoSession implements IKryoSession {
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(KryoSession.class);

    final KryoObjManager manager;
    final ITcpSession session;

    KryoSession(KryoObjManager _manager, ITcpSession _session){
        manager = _manager;
        session = _session;
    }

    /**
     * 发送对象
     * @param _kryo_obj
     */
    public void send(KryoObj _kryo_obj){
        logger.debug("send.OBJ {}", _kryo_obj);
        session.write(manager.obj2hex(_kryo_obj));
    }

    /**
     * 取本地地址
     * @return 本地地址
     */
    public SocketAddress getLocalAddress(){
        return session.getLocalAddress();
    }

    /**
     * 取远端地址
     * @return 远程地址
     */
    public SocketAddress getRemoteAddress(){
        return session.getRemoteAddress();
    }

    /**
     * 取关键字
     * @return  关键字
     */
    public String getSessionKey(){
        return session.getSessionKey();
    }

    @Override
    public String toString(){
        return session.getSessionKey();
    }
}
