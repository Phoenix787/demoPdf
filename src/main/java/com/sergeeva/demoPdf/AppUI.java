package com.sergeeva.demoPdf;


import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.annotations.Widgetset;
import com.vaadin.server.Page;
import com.vaadin.server.StreamResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.spring.server.SpringVaadinServlet;
import com.vaadin.ui.*;
import com.vaadin.ui.themes.ValoTheme;
import com.whitestein.vaadin.widgets.wtpdfviewer.WTPdfViewer;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.easyuploads.UploadField;
import com.google.gwt.safehtml.shared.SafeHtml;

import javax.servlet.annotation.WebServlet;
import java.io.*;
import java.util.concurrent.atomic.AtomicReference;

@SpringUI
@Theme("pdfdemotheme")
@Widgetset("AppWidgetset")
public class AppUI extends UI implements Upload.Receiver, Upload.SucceededListener, Upload.FailedListener {

    private final AtomicReference<TabSheet> sheet = new AtomicReference<TabSheet>();
//
//    @Autowired
//    private EventBus eventBus;


    @Override
    protected void init(VaadinRequest request) {

        Page.getCurrent().setTitle("Demo pdf");

        String filename = "pdf.pdf";
        PdfLink e = new PdfLink(filename);

        VerticalLayout root = new VerticalLayout(e);
        root.setSizeFull();

        final Upload uploadField = new Upload();
        uploadField.setReceiver(this);
        uploadField.setImmediateMode(false);


        WTPdfViewer pdfViewer = new WTPdfViewer();
        pdfViewer.setSizeFull();
        InputStream dataStream = getClass().getClassLoader().getResourceAsStream(filename);

        // show file in pdf viewer
        pdfViewer.setResource(new StreamResource(new InputStreamSource(dataStream), filename));

        DndField dndField = new DndField();


        sheet.set(new TabSheet());
        sheet.get().setSizeFull();
        sheet.get().setStyleName("framed padded-tabbar");
        sheet.get().addComponents(root, uploadField, pdfViewer);
        sheet.get().addTab(root, "Open Link PDF");
        sheet.get().addTab(uploadField, "Upload file");
        sheet.get().addTab(pdfViewer, "Pdf Viewer");
        sheet.get().addTab(dndField, "Drag-And-Drop Zone");



//        WTPdfViewer pdfViewer = new WTPdfViewer();
//        pdfViewer.setSizeFull();
//
//// get file stream
//        String filename = "pdf.pdf";
//        InputStream dataStream = getClass().getClassLoader().getResourceAsStream(filename);
//
//// show file in pdf viewer
//        pdfViewer.setResource(new StreamResource(new InputStreamSource(dataStream), filename));

//        StreamResource.StreamSource s = (StreamResource.StreamSource) () -> {
//            try {
//                String filename = "pdf.pdf";
//                return (FileInputStream) getClass().getClassLoader().getResourceAsStream(filename);
//            } catch (Exception e) {
//                e.printStackTrace();
//                return null;
//            }
//        };
//
//        StreamResource r = new StreamResource(s, "pdf.pdf");
//        Embedded e = new Embedded();
//        e.setSizeFull();
//        e.setType(Embedded.TYPE_BROWSER);
//        r.setMIMEType("application/pdf");
//
//        e.setSource(r);


        setContent(sheet.get());
    }

    @Override
    public OutputStream receiveUpload(String filename, String mimeType) {
        //куда сохраняем загружаемый файл
        FileOutputStream fos;
        File file = new File(AppUIServlet.getCurrent().getServletContext().getRealPath("/tmp")+ File.separator + filename);
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }

        return fos;
    }

    @Override
    public void uploadSucceeded(Upload.SucceededEvent event) {
        Notification.show("Файл " + event.getFilename() + " успешно загружен.", Notification.Type.HUMANIZED_MESSAGE);
    }

    @Override
    public void uploadFailed(Upload.FailedEvent event) {
        Notification.show("Произошла ошибка при загрузке файла " + event.getFilename(), Notification.Type.ERROR_MESSAGE);
    }


    @WebServlet(urlPatterns = "/*", name = "AppUIServlet", asyncSupported = true)
    @VaadinServletConfiguration(ui = AppUI.class, productionMode = true)
    public static class AppUIServlet extends SpringVaadinServlet {
    }

}
