package com.test;

import java.lang.instrument.Instrumentation;
import java.lang.instrument.UnmodifiableClassException;

public class SimpleAgent {
    public static void premain(String agentArgs, Instrumentation inst) {
        System.out.println("premain");
        inst.addTransformer(new PreTransformer());
    }

    public static void agentmain(String agentArgs, Instrumentation inst) throws ClassNotFoundException, UnmodifiableClassException {
        System.out.println("agentmain");
        inst.addTransformer(new AttachTransformer(), true);
        inst.retransformClasses(Class.forName("com.test.JavaTest"));
    }
}
