import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class Test {
    private final static String path = "payload.bin";

    public static void main(String[] args) throws Exception {
        FileInputStream fileInputStream = new FileInputStream(path);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        objectInputStream.readObject();
        fileInputStream.close();
        objectInputStream.close();
    }
}
