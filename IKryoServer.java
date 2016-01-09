package com.etrans.lib.kryo;

/**
 * Created by maotz on 2015-03-31.
 * 服务端接口
 */
public interface IKryoServer<T> {
    /**
     * 注册监听事件
     * @param _listener 事件
     */
    void registerListener(IKryoListener<T> _listener);
    /**
     * 设置监听端口
     * @param _port 端口
     */
    void setPort(int _port);
    /**
     * 开启监听
     * @param _active 开启
     * @throws Exception
     */
    void setActive(boolean _active) throws Exception;
}
