import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IRemoteObj extends Remote {
    public String sayHello(String keywords) throws RemoteException;
}
