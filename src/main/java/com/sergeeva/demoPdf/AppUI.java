package com.sergeeva.demoPdf;


import com.google.gwt.event.shared.EventBus;
import com.vaadin.annotations.Theme;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.whitestein.vaadin.widgets.wtpdfviewer.WTPdfViewer;


import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@SpringUI
@Theme("valo")
public class AppUI extends UI {


    @Override
    protected void init(VaadinRequest request) {

//        WTPdfViewer pdfViewer = new WTPdfViewer();
//        pdfViewer.setSizeFull();
//
//// get file stream
//        String filename = "pdf.pdf";
//        InputStream dataStream = getClass().getClassLoader().getResourceAsStream(filename);
//
//// show file in pdf viewer
//        pdfViewer.setResource(new StreamResource(new InputStreamSource(dataStream), filename));

        StreamResource.StreamSource s = (StreamResource.StreamSource) () -> {
            try {
                File f = new File("D:/homework/demoPdf/src/main/resources/pdf.pdf");
                FileInputStream fis = new FileInputStream(f);
                return fis;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        };

        StreamResource r = new StreamResource(s, "pdf.pdf");
        Embedded e = new Embedded();
        e.setSizeFull();
        e.setType(Embedded.TYPE_BROWSER);
        r.setMIMEType("application/pdf");

        e.setSource(r);

        VerticalLayout root = new VerticalLayout(e);

        root.setSizeFull();

        setContent(root);
    }
}
