import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteObjImpl extends UnicastRemoteObject implements IRemoteObj {

    public RemoteObjImpl() throws RemoteException {
    }

    public String sayHello(String keywords) {
        String upperCase = keywords.toUpperCase();
        System.out.println(upperCase);
        return upperCase;
    }
}
