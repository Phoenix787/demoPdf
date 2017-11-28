package com.sergeeva.demoPdf;

import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.StreamResource;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.OutputStream;

public class PdfLink extends Link {

    private String filename;

    public PdfLink(String filename) {
        super();
        this.filename = filename;
        setCaption ( "Открыть документ" );
        setDescription ( "Открыть документ в новом окне" );
        setTargetName ( "_blank" );

    }

    @Override
    public void attach() {
        super.attach();
        StreamResource.StreamSource source = (StreamResource.StreamSource) () -> {
            try {
                return getClass().getClassLoader().getResourceAsStream(filename);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        };

        StreamResource resource = new StreamResource(source, filename);
//        Embedded e = new Embedded();
//        e.setSizeFull();
//        e.setType(Embedded.TYPE_BROWSER);
        resource.setMIMEType("application/pdf");

        setResource(resource);
        markAsDirty();

    }
}
