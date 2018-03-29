package com.softwarelma.epe.p3.xml;

import java.io.BufferedReader;
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

public final class EpeXmlFinalXml_read_site extends EpeXmlAbstract {

    // maybe also nonProxyHosts and java.net.useSystemProxies
    public static final String PROP_PROXY_HOST = "proxyHost";
    public static final String PROP_PROXY_PORT = "proxyPort";
    public static final String PROP_PROXY_USER = "proxyUser";
    public static final String PROP_PROXY_PASSWORD = "proxyPassword";

    /*
     * "http.nonProxyHosts", "http.proxyHost", "http.proxyPort", "http.proxyUser", "http.proxyPassword",
     * "https.nonProxyHosts", "https.proxyHost", "https.proxyPort", "https.proxyUser", "https.proxyPassword",
     * "java.net.useSystemProxies"
     */

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "xml_read_site, expected the URL.";
        String host = retrievePropValueOrNull("xml_read_site", listExecResult, PROP_PROXY_HOST);
        String port = retrievePropValueOrNull("xml_read_site", listExecResult, PROP_PROXY_PORT);
        String username = retrievePropValueOrNull("xml_read_site", listExecResult, PROP_PROXY_USER);
        String password = retrievePropValueOrNull("xml_read_site", listExecResult, PROP_PROXY_PASSWORD);
        List<String> listSite = readSite(execParams, listExecResult, postMessage, host, port, username, password);

        if (listSite.size() == 1) {
            String str = listSite.get(0);
            log(execParams, str);
            return createResult(str);
        }

        log(execParams, listSite);
        return createResult(listSite);
    }

    public List<String> readSite(EpeExecParams execParams, List<EpeExecResult> listExecResult, String postMessage,
            String host, String port, String username, String password) throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        List<String> listSite = new ArrayList<>();
        String url;

        for (int i = 0; i < listExecResult.size(); i++) {
            if (isPropAt(listExecResult, i, postMessage)) {
                continue;
            }

            url = getStringAt(listExecResult, i, postMessage);
            log(execParams, "URL: " + url);
            String site = readSite(url, host, port, username, password);
            log(execParams, "Content:");
            log(execParams, site);
            listSite.add(site);
        }

        return listSite;
    }

    // TODO post, from:
    // https://stackoverflow.com/questions/4205980/java-sending-http-parameters-via-post-method-easily
    // or:
    // https://www.mkyong.com/java/how-to-send-http-request-getpost-in-java/

    /*
     * from:
     * 
     * 1- Java Networking and Proxies: http://docs.oracle.com/javase/7/docs/technotes/guides/net/proxies.html
     * 
     * 2- stackoverflow example: https://stackoverflow.com/questions/1432961/how-do-i-make-httpurlconnection-use-a-proxy
     */

    public static String readSite(String url, String host, String port, String username, String password)
            throws EpeAppException {
        try {
            Proxy proxy = retrieveProxy(host, port, username, password);
            URLConnection conn = proxy == null ? new URL(url).openConnection() : new URL(url).openConnection(proxy);
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder sb = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
                sb.append("\n");
            }

            in.close();
            return sb.toString();
        } catch (IOException e) {
            throw new EpeAppException("readSite url: " + url, e);
        }
    }

    @Deprecated
    public static String readSiteOld(String url) throws EpeAppException {
        EpeAppUtils.checkEmpty("url", url);

        try {
            URL oracle = new URL(url);
            BufferedReader in = new BufferedReader(new InputStreamReader(oracle.openStream()));
            String inputLine;
            StringBuilder sb = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                sb.append(inputLine);
                sb.append("\n");
            }

            in.close();
            return sb.toString();
        } catch (MalformedURLException e) {
            throw new EpeAppException("readSite url: " + url, e);
        } catch (IOException e) {
            throw new EpeAppException("readSite url: " + url, e);
        }
    }

    public static Proxy retrieveProxy(String host, String port, String username, String password)
            throws EpeAppException {
        if (host == null || port == null) {
            return null;
        }

        setAuthenticator(username, password);
        int portInt = EpeAppUtils.parseInt(port);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, portInt));
        return proxy;
    }

    public static void setAuthenticator(final String username, final String password) {
        if (username == null || password == null) {
            return;
        }

        Authenticator authenticator = new Authenticator() {

            public PasswordAuthentication getPasswordAuthentication() {
                // or "domainName\\user"
                return (new PasswordAuthentication(username, password.toCharArray()));
            }

        };

        Authenticator.setDefault(authenticator);
    }

}
