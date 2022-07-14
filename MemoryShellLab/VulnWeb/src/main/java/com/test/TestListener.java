package com.test;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class TestListener implements ServletRequestListener {
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
//        ServletRequestListener.super.requestDestroyed(sre);
        System.out.println("Listener destroy");
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
//        ServletRequestListener.super.requestInitialized(sre);
        System.out.println("Listener init");
    }
}
