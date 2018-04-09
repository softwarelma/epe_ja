package com.softwarelma.epe.p3.xml;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeXmlFinalXml_write_site extends EpeXmlAbstract {

    /**
     * http://www.baeldung.com/httpclient-multipart-upload
     */
    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "xml_write_site, expected the URL.";
        // log(execParams, listSite);
        // return createResult(listSite);
    }

    /**
     * Uploading a Form with Two Text Parts and a File
     */
    public static void m1() {
        File file = new File(textFileName);
        HttpPost post = new HttpPost("http://echo.200please.com");
        FileBody fileBody = new FileBody(file, ContentType.DEFAULT_BINARY);
        StringBody stringBody1 = new StringBody("Message 1", ContentType.MULTIPART_FORM_DATA);
        StringBody stringBody2 = new StringBody("Message 2", ContentType.MULTIPART_FORM_DATA);
        //
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addPart("upfile", fileBody);
        builder.addPart("text1", stringBody1);
        builder.addPart("text2", stringBody2);
        HttpEntity entity = builder.build();
        //
        post.setEntity(entity);
        HttpResponse response = client.execute(post);
    }

    /**
     * Uploading Text and a Text File Part
     */
    public static void m2() {
        HttpPost post = new HttpPost("http://echo.200please.com");
        File file = new File(textFileName);
        String message = "This is a multipart post";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody("upfile", file, ContentType.DEFAULT_BINARY, textFileName);
        builder.addTextBody("text", message, ContentType.DEFAULT_BINARY);
        //
        HttpEntity entity = builder.build();
        post.setEntity(entity);
        HttpResponse response = client.execute(post);
    }

    /**
     * Uploading a Zip File, an Image File, and a Text Part
     */
    public static void m3() {
        HttpPost post = new HttpPost("http://echo.200please.com");
        InputStream inputStream = new FileInputStream(zipFileName);
        File file = new File(imageFileName);
        String message = "This is a multipart post";
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody("upfile", file, ContentType.DEFAULT_BINARY, imageFileName);
        builder.addBinaryBody("upstream", inputStream, ContentType.create("application/zip"), zipFileName);
        builder.addTextBody("text", message, ContentType.TEXT_PLAIN);
        //
        HttpEntity entity = builder.build();
        post.setEntity(entity);
        HttpResponse response = client.execute(post);
    }

    /**
     * Uploading a Byte Array and Text
     */
    public static void m4() {
        HttpPost post = new HttpPost("http://echo.200please.com");
        String message = "This is a multipart post";
        byte[] bytes = "binary code".getBytes();
        //
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody("upfile", bytes, ContentType.DEFAULT_BINARY, textFileName);
        builder.addTextBody("text", message, ContentType.TEXT_PLAIN);
        //
        HttpEntity entity = builder.build();
        post.setEntity(entity);
        HttpResponse response = client.execute(post);
    }

}
