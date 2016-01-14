
import java.util.LinkedList;
import java.util.List;

/**
 * Created by maotz on 2015-03-20.
 *
 */
public class KryoListeners<T> implements IKryoListener<T> {
    private final List<IKryoListener<T>> list = new LinkedList<>();

    public void registerListener(IKryoListener<T> _listener){
        if(null!=_listener && !list.contains(_listener))
            list.add(_listener);
    }

    public void onConn(T _sender){
        for(IKryoListener<T> listener : list)
            listener.onConn(_sender);
    }
    public void onBrok(T _sender){
        for(IKryoListener<T> listener : list)
            listener.onBrok(_sender);
    }
    public void onData(T _sender, KryoObj _obj){
        for(IKryoListener<T> listener : list)
            listener.onData(_sender, _obj);
    }
}
