package com.etrans.lib.kryo;

import com.etrans.lib.net.tcp.ITcpListener;
import com.etrans.lib.net.tcp.ITcpServer;
import com.etrans.lib.net.tcp.ITcpSession;
import com.etrans.lib.net.tcp.aio.AioTcpServer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by maotz on 2015-03-20.
 * TCP 对象 传输 服务端
 */
public class KryoServer implements IKryoServer<IKryoSession>{
    /**
     * 日志记录对象
     */
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(KryoServer.class);

    /**
     * 自身引用
     */
    private final KryoServer self = this;

    /**
     * 服务端对象 按回车换行分隔
     */
    private final ITcpServer tcpServer = new AioTcpServer(true);

    /**
     * 事件管理器
     */
    private final KryoListeners<IKryoSession> listeners = new KryoListeners<IKryoSession>();

    /**
     * 对象管理器
     */
    private final KryoObjManager objManager;

    /**
     * 链接管理器
     */
    private final Map<ITcpSession, KryoSession> links = new ConcurrentHashMap<ITcpSession, KryoSession>();

    /**
     * 构造函数
     * @param _obj_manager 对象管理器
     */
    public KryoServer(KryoObjManager _obj_manager){
        objManager  = _obj_manager;

        tcpServer.addListener(new ITcpListener<ITcpSession>() {
            public void onConn(ITcpSession _sender, boolean _success, Exception _e) {
                if (_success) {
                    logger.info("onConn {}", _sender);
                    KryoSession session = new KryoSession(objManager.copy(), _sender);
                    links.put(_sender, session);
                    listeners.onConn(session);
                } else {
                    logger.error("error on link {}, {}", _sender, _e);
                }
            }

            public void onBrok(ITcpSession _sender) {
                logger.info("onBrok {}", _sender);
                KryoSession link = links.get(_sender);
                listeners.onBrok(link);
                links.remove(_sender);
            }

            public void onData(ITcpSession _sender, Object _data) {
                KryoSession session = links.get(_sender);
                if (null != session) {
                    String line = (String) _data;
                    try {
                        listeners.onData(session, session.manager.hex2Obj(line));
                    } catch (Exception e) {
                        logger.error("onData {} {}", line, e);
                    }
                } else {
                    logger.info("unknown session data {} {}", _sender, _data);
                }
            }
        });

    }

    /**
     * 设置监听端口
     * @param _port 端口
     */
    public void setPort(int _port){
        tcpServer.setListenPort(_port);
    }

    /**
     * 开启监听
     * @param _active 开启
     * @throws Exception
     */
    public void setActive(boolean _active) throws Exception{
        tcpServer.setActive(_active);
    }

    /**
     * 注册监听事件
     * @param _listener 事件
     */
    public void registerListener(IKryoListener<IKryoSession> _listener){
        listeners.registerListener(_listener);
    }

}
