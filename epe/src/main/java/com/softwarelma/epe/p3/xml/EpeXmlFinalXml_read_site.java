package com.softwarelma.epe.p3.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p1.app.EpeAppUtils;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

public final class EpeXmlFinalXml_read_site extends EpeXmlAbstract {

    // non proxy syntax: localhost|127.0.0.1
    public static final String[] PROPS_PROXY = { "http.nonProxyHosts", "http.proxyHost", "http.proxyPort",
            "http.proxyUser", "http.proxyPassword", "https.nonProxyHosts", "https.proxyHost", "https.proxyPort",
            "https.proxyUser", "https.proxyPassword" };

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "xml_read_site, expected the URL.";
        this.setSystemProxy(listExecResult);
        List<String> listSite = this.readSite(execParams, listExecResult, postMessage);
        this.log(execParams, listSite);
        return this.createResult(listSite);
    }

    public static void setSystemProxy(List<EpeExecResult> listExecResult) throws EpeAppException {
        for (String propKey : PROPS_PROXY) {
            String propVal = retrievePropValueOrNull("xml_read_site", listExecResult, propKey);

            if (propVal != null) {
                System.setProperty(propKey, propVal);
            }
        }
    }

    public List<String> readSite(EpeExecParams execParams, List<EpeExecResult> listExecResult, String postMessage)
            throws EpeAppException {
        EpeAppUtils.checkNull("listExecResult", listExecResult);
        List<String> listSite = new ArrayList<>();
        String url;

        for (int i = 0; i < listExecResult.size(); i++) {
            if (this.isPropAt(listExecResult, i, postMessage)) {
                continue;
            }

            url = this.getStringAt(listExecResult, i, postMessage);
            this.log(execParams, "URL: " + url);
            String site = this.readSite(url);
            this.log(execParams, "Content:");
            this.log(execParams, site);
            listSite.add(site);
        }

        return listSite;
    }

    public static String readSite(String url) throws EpeAppException {
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

}
