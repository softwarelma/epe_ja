package com.softwarelma.epe.p3.xml;

import java.io.IOException;
import java.io.StringWriter;

import org.w3c.dom.Document;

import com.sun.org.apache.xml.internal.serialize.OutputFormat;
import com.sun.org.apache.xml.internal.serialize.XMLSerializer;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker.State;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class AppFx extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        String str = null;
        WebView webview = new WebView();
        final WebEngine webengine = webview.getEngine();
        webengine.setJavaScriptEnabled(true);
        webengine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
            public void changed(ObservableValue ov, State oldState, State newState) {
                System.out.println("newState: " + newState);
                if (State.SUCCEEDED.equals(newState)) {
                    Document doc = webengine.getDocument();
                    // Serialize DOM
                    OutputFormat format = new OutputFormat(doc);
                    // as a String
                    StringWriter stringOut = new StringWriter();
                    XMLSerializer serial = new XMLSerializer(stringOut, format);
                    try {
                        serial.serialize(doc);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Display the XML
                    System.out.println(stringOut.toString());// FIXME
                }
            }
        });
        // webengine.load("http://detail.tmall.com/item.htm?spm=a220o.1000855.0.0.PZSbaQ&id=19378327658");
        webengine.load("http://www.softwarelma.com/translator/");
    }

}
