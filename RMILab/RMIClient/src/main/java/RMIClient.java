import java.lang.reflect.Proxy;
import java.rmi.Remote;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIClient {
    public static void main(String[] args) throws Exception {
        Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1099);
        IRemoteObj remoteObj = (IRemoteObj) registry.lookup("remoteObj");
        remoteObj.sayHello("hello");
    }
}
