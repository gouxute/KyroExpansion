package com.etrans.lib.kryo;

import com.etrans.lib.net.tcp.ITcpClient;
import com.etrans.lib.net.tcp.ITcpListener;
import com.etrans.lib.net.tcp.aio.AioTcpClient;

/**
 * Created by maotz on 2015-03-20
 * Kryo 对象传输 客户端
 */
public class KryoClient implements IKryoClient{
    /**
     * 日志记录对象
     */
    private final static org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(KryoClient.class);

    /**
     * 自身引用
     */
    private final KryoClient self = this;

    /**
     * 对象管理器
     */
    private final KryoObjManager objManager;

    /**
     * 链接对象，按回车换行分隔字符串
     */
    private final ITcpClient tcpClient= new AioTcpClient(true);

    /**
     * 事件监听器
     */
    private final KryoListeners<IKryoClient> listeners = new KryoListeners<>();

    /**
     * 构造函数
     * @param _obj_manager 对象管理器
     */
    public KryoClient(KryoObjManager _obj_manager){
        objManager  = _obj_manager;

        tcpClient.addListener(new ITcpListener<ITcpClient>() {
            public void onConn(ITcpClient _sender, boolean _success, Exception _e) {
                if (_success) {
                    listeners.onConn(self);
                } else {
                    logger.error("error on conn {}, {}", _sender, _e);
                }
            }

            public void onBrok(ITcpClient _sender) {
                listeners.onBrok(self);
            }

            public void onData(ITcpClient _sender, Object _data) {
                String line = (String) _data;
                try {
                    listeners.onData(self, objManager.hex2Obj(line));
                } catch (Exception e) {
                    logger.error("error on data {} {}", line, e);
                }
            }
        });
    }

    /**
     * 发送对象
     * @param _kryo_obj 数据对象
     */
    public void send(KryoObj _kryo_obj){
        //logger.info("send obj {}", _obj);;
        tcpClient.write(objManager.obj2hex(_kryo_obj));
    }

    /**
     *
     * @param _host 主机
     * @param _port 端口
     */
    public void setServer(String _host, int _port){
        tcpClient.setServer(_host, _port);
    }

    /**
     * 开启链接
     * @param _active 开/关
     */
    public void setActive(boolean _active){
        tcpClient.setActive(_active);
    }

    /**
     * 判断是否链接成功
     * @return 链接成功
     */
    public boolean isConnected(){
        return tcpClient.isConnected();
    }

    /**
     * 注册监听器
     * @param _listener 监听器
     */
    public void registerListeher(IKryoListener<IKryoClient> _listener){
        listeners.registerListener(_listener);
    }

    @Override
    public String toString(){
        return tcpClient.toString();
    }

}
