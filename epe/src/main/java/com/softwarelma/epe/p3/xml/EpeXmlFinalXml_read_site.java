package com.softwarelma.epe.p3.xml;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import com.softwarelma.epe.p1.app.EpeAppException;
import com.softwarelma.epe.p2.exec.EpeExecParams;
import com.softwarelma.epe.p2.exec.EpeExecResult;

import javafx.application.Application;

public final class EpeXmlFinalXml_read_site extends EpeXmlAbstract {

    @Override
    public EpeExecResult doFunc(EpeExecParams execParams, List<EpeExecResult> listExecResult) throws EpeAppException {
        String postMessage = "xml_read_site, expected the URL and optionally the \"dynamic\" mode.";
        String url = this.getStringAt(listExecResult, 0, postMessage);
        this.log(execParams, "URL: " + url);
        String dynamic = this.getStringAt(listExecResult, 1, postMessage, null);
        String str;

        if (dynamic == null) {
            str = this.readSiteDynamic(url);
        } else {
            str = this.readSite(url);
        }

        this.log(execParams, str);
        return this.createResult(str);
    }

    private String readSite(String url) throws EpeAppException {
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

    private String readSiteDynamic(String url) {
        // Application.launch(AppFx.class, null);
        // Application.launch(WebViewSample.class, null);
        Application.launch(AppFx2.class, null);
        return null;
    }

}
