<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.*,java.util.*" %>
<%@ page import="sun.misc.BASE64Decoder" %>
<html>
<head><title>Hello World</title></head>
<body>
There is a deserialization point!<br/>
Input parameter:
<%= request.getParameter("a") %>
<br/>
<!--
Base64 decode:
<%
    if (request.getParameter("a") != null) {
        byte[] bytes = new BASE64Decoder().decodeBuffer(request.getParameter("a"));
        out.println(new String(bytes));
    }
    else out.println("null");
%>
<br/>
-->
<%
    String a = request.getParameter("a");
    Thread.currentThread().getContextClassLoader();
    if (a != null) {
        byte[] bytes = new BASE64Decoder().decodeBuffer(a);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Object obj = objectInputStream.readObject();
    }
%>
</body>
</html>