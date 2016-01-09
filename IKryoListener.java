package com.etrans.lib.kryo;

/**
 * Created by maotz on 2015-03-20.
 *
 */
public interface IKryoListener<T> {
    void onConn(T _sender);
    void onBrok(T _sender);
    void onData(T _sender, KryoObj _obj);
}
