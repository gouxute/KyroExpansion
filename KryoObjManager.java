package com.etrans.lib.kryo;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.etrans.lib.utils.Coder;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by maotz on 2015-03-19.
 *
 */
public class KryoObjManager {
    private static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(KryoObjManager.class);

    /**
     * 所有类型集合
     */
    private final List<Class> clsList = new LinkedList<Class>();
    /**
     * 下行对象解析器
     */
    private final Kryo kryoDn = new Kryo();
    private final Output output = new Output(32, -1);

    /**
     * 上行对象解析器
     */
    private final Kryo kryoUp = new Kryo();
    private final Input input = new Input();

    public KryoObjManager(){
    }

    /**
     * 注册所关注的类型
     * @param _cls 类型
     */
    public void registerClass(Class _cls){
        if(null!=_cls && !clsList.contains(_cls)) {
            clsList.add(_cls);
            kryoDn.register(_cls);
            kryoUp.register(_cls);
        }
    }

    /**
     * 字符串化对象，格式：类型【2】+内容【*】{ + CRLF 已经去掉 }
     * @param _hex 字符串
     * @return 对象
     * @throws Exception
     */
    KryoObj hex2Obj(String _hex) throws Exception{
        byte[] bytes = Coder.hexStringToByteArray(_hex);
        int index = (bytes[0] << 8) + bytes[1]; // 最前面 两个字节，表示类型
        Class cls = clsList.get(index);
        input.setBuffer(bytes, 2, bytes.length - 2);
        return (KryoObj) kryoUp.readObject(input, cls);
    }

    /**
     * 对象化字符串，格式：类型【2】+内容【*】+ CRLF
     * @param _kryo_obj
     * @return 字符串
     */
    String obj2hex(KryoObj _kryo_obj){
        output.clear();
        kryoDn.writeObject(output, _kryo_obj);
        int index = clsList.indexOf(_kryo_obj.getClass());
        String pex = String.format("%04X", index);
        String hex = Coder.byteArrayToHex(output.toBytes());
        return pex + hex + "\r\n";
    }

    /**
     * 对象拷贝
     * @return 新的管理器
     */
    KryoObjManager copy(){
        KryoObjManager manager = new KryoObjManager();
        for(Class cls : clsList) {
            manager.registerClass(cls);
        }
        return manager;
    }
}
