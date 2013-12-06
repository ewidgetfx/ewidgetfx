package org.ewidgetfx.widget.conferencetour;

import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.TitledPane;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

public class ConferenceTour extends Pane {

    public static void main(String[] args) {

    }

    WebEngine engine;

    public ConferenceTour() {
        setWidth(700);
        setHeight(450);
        getChildren().add(createMap());
    }

    private BorderPane createMap() {
        BorderPane inner = new BorderPane();
        inner.setCenter(createWebView());
        inner.setLeft(createConferences());
        BorderPane outer = new BorderPane();
        AnchorPane anchor = new AnchorPane();
        anchor.getChildren().add(inner);
        AnchorPane.setBottomAnchor(inner, 100d);
        AnchorPane.setTopAnchor(inner, 30d);
        AnchorPane.setLeftAnchor(inner, 30d);
        AnchorPane.setRightAnchor(inner, 30d);
        outer.setCenter(anchor);
        return outer;
    }

    private WebView createWebView() {
        WebView webView = new WebView();
        engine = webView.getEngine();
        webView.getEngine().load(getClass().getResource("content.html").toString());
        webView.setEffect(new Reflection(10, .5, .75, 0));
        return webView;
    }

    private Accordion createConferences() {
        Accordion accordion = new Accordion();
        final TitledPane india = createConference("JavaOne India", 17.385371, 78.484268, "http://steveonjava.com/wp-content/uploads/2011/03/javaone-india.png");
        accordion.getPanes().add(india);
        accordion.getPanes().add(createConference("OSCON", 45.515008, -122.693253, "http://steveonjava.com/wp-content/uploads/2011/05/oscon.png"));
        accordion.getPanes().add(createConference("Devoxx", 51.206883, 4.44, "http://steveonjava.com/wp-content/uploads/2010/07/LogoDevoxxNeg150.png"));
        accordion.getPanes().add(createConference("J-Fall", 52.219913, 5.474253, "http://steveonjava.com/wp-content/uploads/2011/11/jfall3.png"));
        accordion.getPanes().add(createConference("JavaOne SF", 37.775057, -122.416534, "http://steveonjava.com/wp-content/uploads/2010/07/JavaOne-2010-Speaker.png"));
        accordion.getPanes().add(createConference("Jazoon", 47.382079, 8.528137, "http://steveonjava.com/wp-content/uploads/2010/04/jazoon.png"));
        accordion.getPanes().add(createConference("GeeCON", 50.064633, 19.949799, "http://steveonjava.com/wp-content/uploads/2011/03/geecon.png"));
        india.setExpanded(true);
        accordion.setExpandedPane(india);
        accordion.expandedPaneProperty().addListener((ObservableValue<? extends TitledPane> ov, TitledPane t, TitledPane t1) -> {
            if (t1 != null) {
                ((ConferencePane) t1).navigateTo();
            }
        });
        return accordion;
    }

    private ConferencePane createConference(String name, final double lat, final double lon, String imageUrl) {
        return new ConferencePane(name, new ImageView(new Image(imageUrl)), lat, lon);
    }

    public class ConferencePane extends TitledPane {

        private final double lat;
        private final double lon;

        private ConferencePane(String label, Node node, double lat, double lon) {
            super(label, node);
            this.lat = lat;
            this.lon = lon;
        }

        public void navigateTo() {
            engine.executeScript("moveMap(" + lat + ", " + lon + ");");
        }
    }
}
