package com.test;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class PreTransformer implements ClassFileTransformer {

    public static final String ClassName = "com.test.JavaTest";

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        className = className.replace("/", ".");
        if(className.equals(ClassName)) {
            System.out.println("Got it");
            ClassPool classPool = ClassPool.getDefault();
            try {
                CtClass ctClass = classPool.getCtClass(className);
                CtMethod method = ctClass.getDeclaredMethod("test");
                method.insertBefore("System.out.println(\"insert success\");");
                byte[] bytes = ctClass.toBytecode();
                ctClass.detach();
                return bytes;
            } catch (NotFoundException | CannotCompileException | IOException e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }
}
