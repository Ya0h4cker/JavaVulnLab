import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class RMIServer {
    public static void main(String[] args) throws Exception {
        IRemoteObj remoteObj = new RemoteObjImpl();
        Registry registry = LocateRegistry.createRegistry(1099);
        registry.bind("remoteObj", remoteObj);
    }
}
