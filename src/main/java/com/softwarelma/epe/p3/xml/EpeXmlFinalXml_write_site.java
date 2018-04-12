package com.softwarelma.epe.p3.xml;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.apache.http.util.EntityUtils;

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
        String postMessage = "xml_write_site, expected ???.";
        boolean apache = true;
        String host = retrievePropValueOrNull("xml_read_site", listExecResult,
                EpeXmlFinalXml_read_site.PROP_PROXY_HOST);
        String port = retrievePropValueOrNull("xml_read_site", listExecResult,
                EpeXmlFinalXml_read_site.PROP_PROXY_PORT);
        String username = retrievePropValueOrNull("xml_read_site", listExecResult,
                EpeXmlFinalXml_read_site.PROP_PROXY_USER);
        String password = retrievePropValueOrNull("xml_read_site", listExecResult,
                EpeXmlFinalXml_read_site.PROP_PROXY_PASSWORD);
        EpeXmlProxy proxy = host == null || port == null ? null : new EpeXmlProxy(host, port, username, password);

        if (apache) {
            String textFileName = "file";
            List<String> statusCodeAndStatusLineAndBody = m4(proxy, textFileName);
            List<String> listStr = new ArrayList<>();
            listStr.add("\n\tstatusCode=" + statusCodeAndStatusLineAndBody.get(0));
            listStr.add("\n\tstatusLine=" + statusCodeAndStatusLineAndBody.get(1));
            listStr.add("\n\tbody=" + statusCodeAndStatusLineAndBody.get(2));
            log(execParams, listStr);
            return createResult(statusCodeAndStatusLineAndBody);
        }

        m5();
        // m6();

        return createEmptyResult();
    }

    public static void m6() throws EpeAppException {
        try {
            String urlParameters = "text1=abc&text2=def";
            URL url = new URL("http://echo.200please.com");
            URLConnection conn = url.openConnection();
            ((HttpURLConnection) conn).setInstanceFollowRedirects(false);

            conn.setDoOutput(true);

            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

            writer.write(urlParameters);
            writer.flush();

            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
            writer.close();
            reader.close();
        } catch (Exception e) {
            throw new EpeAppException("???", e);
        }
    }

    public static void m5() throws EpeAppException {
        String request = "http://echo.200please.com";

        try {
            String urlParameters = "text1=abc&text2=def";
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            URL url = new URL(request);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "UTF-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.setUseCaches(false);

            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
                Reader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                StringBuilder sb = new StringBuilder();
                for (int c; (c = in.read()) >= 0;)
                    sb.append((char) c);
                System.out.println(sb);
            }
        } catch (Exception e) {
            throw new EpeAppException("???", e);
        }
    }

    public static List<String> retrieveStatusCodeAndStatusLineAndBody(HttpResponse response) throws EpeAppException {
        String step = "";
        List<String> statusCodeAndStatusLineAndBody = new ArrayList<>(3);

        try {
            step = "getEntity";
            HttpEntity entity = response.getEntity();
            step = "getStatusCode";
            statusCodeAndStatusLineAndBody.add(response.getStatusLine().getStatusCode() + "");
            step = "getStatusLine";
            statusCodeAndStatusLineAndBody.add(response.getStatusLine().toString());
            step = "getBody";
            statusCodeAndStatusLineAndBody.add(EntityUtils.toString(entity));
        } catch (ParseException | IOException e) {
            throw new EpeAppException("readResponse, " + step, e);
        }

        return statusCodeAndStatusLineAndBody;
    }

    /**
     * Uploading a Form with Two Text Parts and a File
     */
    public static List<String> m1(String textFileName) throws EpeAppException {
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
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response;

        try {
            response = client.execute(post);
        } catch (IOException e) {
            throw new EpeAppException("POST, addPart, classical", e);
        }

        return retrieveStatusCodeAndStatusLineAndBody(response);
    }

    /**
     * Uploading Text and a Text File Part
     */
    public static List<String> m2(String textFileName) throws EpeAppException {
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
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response;

        try {
            response = client.execute(post);
        } catch (IOException e) {
            throw new EpeAppException("POST, addBinaryBody and addTextBody, file", e);
        }

        return retrieveStatusCodeAndStatusLineAndBody(response);
    }

    /**
     * Uploading a Zip File, an Image File, and a Text Part
     */
    public static List<String> m3(String zipFileName, String imageFileName) throws EpeAppException {
        HttpPost post = new HttpPost("http://echo.200please.com");
        InputStream inputStream;

        try {
            inputStream = new FileInputStream(zipFileName);
        } catch (IOException e) {
            throw new EpeAppException("POST, addBinaryBody and addTextBody, application/[zip], inputStream", e);
        }

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
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response;

        try {
            response = client.execute(post);
        } catch (IOException e) {
            throw new EpeAppException("POST, addBinaryBody and addTextBody, application/[zip], execute", e);
        }

        return retrieveStatusCodeAndStatusLineAndBody(response);
    }

    /**
     * Uploading a Byte Array and Text
     */
    public static List<String> m4(EpeXmlProxy proxy, String textFileName) throws EpeAppException {
        String message = "This is a multipart post";
        byte[] bytes = "binary code".getBytes();
        //
        MultipartEntityBuilder builder = MultipartEntityBuilder.create();
        builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
        builder.addBinaryBody("upfile", bytes, ContentType.DEFAULT_BINARY, textFileName);
        builder.addTextBody("text", message, ContentType.TEXT_PLAIN);
        //
        HttpClient client = retrieveClientAndSetProxy(proxy);
        HttpEntity entity = builder.build();
        String url = "http://echo.200please.com";
        url = expand(client, url);
        HttpPost post = new HttpPost(url);
        post.setEntity(entity);
        HttpResponse response;

        try {
            response = client.execute(post);
        } catch (IOException e) {
            throw new EpeAppException("POST, addBinaryBody and addTextBody, byte[]", e);
        }

        return retrieveStatusCodeAndStatusLineAndBody(response);
    }

    public static String expand(HttpClient client, String urlArg) throws EpeAppException {
        String originalUrl = urlArg;
        String newUrl = expandSingleLevel(client, originalUrl);
        while (!originalUrl.equals(newUrl)) {
            originalUrl = newUrl;
            newUrl = expandSingleLevel(client, originalUrl);
        }
        return newUrl;
    }

    public static String expandSingleLevel(HttpClient client, String url) throws EpeAppException {
        HttpHead request = null;
        try {
            request = new HttpHead(url);
            HttpResponse httpResponse = client.execute(request);
            int statusCode = httpResponse.getStatusLine().getStatusCode();
            if (statusCode != 301 && statusCode != 302)
                return url;
            Header[] headers = httpResponse.getHeaders(HttpHeaders.LOCATION);
            EpeAppUtils.checkRange(headers.length, 1, 1, false, false, "headers.length", null);
            return headers[0].getValue();// the newUrl
        } catch (IllegalArgumentException uriEx) {
            return url;
        } catch (ClientProtocolException e) {
            throw new EpeAppException(e.getClass().getName(), e);
        } catch (IOException e) {
            throw new EpeAppException(e.getClass().getName(), e);
        } finally {
            if (request != null)
                request.releaseConnection();
        }
    }

    /**
     * from https://www.manthanhd.com/2015/08/24/configuring-java-httpclient-to-use-proxy/
     */
    public static HttpClient retrieveClientAndSetProxy(EpeXmlProxy proxy) throws EpeAppException {
        try {
            if (proxy == null)
                return HttpClientBuilder.create().build();

            // CREDENTIALS
            CredentialsProvider credsProvider = new BasicCredentialsProvider();
            credsProvider.setCredentials(new AuthScope(proxy.getHost(), Integer.parseInt(proxy.getPort())),
                    new UsernamePasswordCredentials(proxy.getUsername(), proxy.getPassword()));

            // PROXY
            HttpHost proxyHostObject = new HttpHost(proxy.getHost(), Integer.parseInt(proxy.getPort()));

            // CLIENT
            HttpClient client = HttpClientBuilder.create().setProxy(proxyHostObject)
                    .setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy())
                    .setDefaultCredentialsProvider(credsProvider).build();
            return client;
        } catch (Exception e) {
            throw new EpeAppException("retrieveClientAndSetProxy", e);
        }
    }

}
