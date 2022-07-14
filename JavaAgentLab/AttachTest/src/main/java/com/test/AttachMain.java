package com.test;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;

public class AttachMain {
    public static void main(String[] args) throws Exception {
        final String path = "D:\\JavaAgentLab\\AgentTest\\target\\AgentTest-1.0-SNAPSHOT-jar-with-dependencies.jar";
        File file = new File(System.getProperty("java.home").replace("jre", "lib") + File.separator + "tools.jar");
        URL url = file.toURI().toURL();
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url});
        Class<?> VirtualMachine = urlClassLoader.loadClass("com.sun.tools.attach.VirtualMachine");
        Class<?> VirtualMachineDescriptor = urlClassLoader.loadClass("com.sun.tools.attach.VirtualMachineDescriptor");
        Method listMethod = VirtualMachine.getDeclaredMethod("list", null);
        List list = (List) listMethod.invoke(VirtualMachine, null);

        for (Object o : list) {
            Method displayName = VirtualMachineDescriptor.getDeclaredMethod("displayName");
            String name = (String) displayName.invoke(o, null);
            System.out.println(name);
            if(name.endsWith("com.test.JavaTest")) {
                Method attach = VirtualMachine.getDeclaredMethod("attach", VirtualMachineDescriptor);
                Object vm = attach.invoke(VirtualMachine, o);
                Method loadAgent = VirtualMachine.getDeclaredMethod("loadAgent", String.class);
                loadAgent.invoke(vm, path);
                Method detach = VirtualMachine.getDeclaredMethod("detach", null);
                detach.invoke(vm, null);
            }
        }
    }
}
