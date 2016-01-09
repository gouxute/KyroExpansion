package com.etrans.lib.kryo;

/**
 * Created by maotz on 2015-03-31.
 * 客户端接口
 */
public interface IKryoClient {
    /**
     * 设置服务器地址
     * @param _host 主机
     * @param _port 端口
     */
    void setServer(String _host, int _port);
    /**
     * 开启链接
     * @param _active 开/关
     */
    void setActive(boolean _active);
    /**
     * 判断是否链接成功
     * @return 链接成功
     */
    boolean isConnected();
    /**
     * 注册监听器
     * @param _listener 监听器
     */
    void registerListeher(IKryoListener<IKryoClient> _listener);
    /**
     * 发送对象
     * @param _kryo_obj 数据对象
     */
    void send(KryoObj _kryo_obj);
}
