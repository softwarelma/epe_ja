package com.softwarelma.epe.p3.xml;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.scene.Scene;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import javafx.application.Application;

public class AppFx2 extends Application {

    public void start(Stage primaryStage) {
        WebView webview = new WebView();
        final WebEngine webengine = webview.getEngine();
        webengine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
            public void changed(ObservableValue ov, State oldState, State newState) {
                if (newState == State.SUCCEEDED) {
                    Document doc = webengine.getDocument();
                    try {
                        Transformer transformer = TransformerFactory.newInstance().newTransformer();
                        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
                        transformer.setOutputProperty(OutputKeys.METHOD, "xml");
                        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
                        transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                        transformer.transform(new DOMSource(doc),
                                new StreamResult(new OutputStreamWriter(System.out, "UTF-8")));
                        // String html = (String) webengine.executeScript("document.documentElement.outerHTML");
                        // System.out.println(html);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        });
        // webengine.load("http://stackoverflow.com");
        webengine.load("http://www.softwarelma.com/translator/");
        primaryStage.setScene(new Scene(webview, 800, 800));
        primaryStage.show();
    }

}
