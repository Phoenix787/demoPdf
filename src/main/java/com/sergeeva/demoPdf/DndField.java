package com.sergeeva.demoPdf;

import com.vaadin.server.StreamVariable;
import com.vaadin.shared.ui.dnd.DropEffect;
import com.vaadin.shared.ui.dnd.EffectAllowed;
import com.vaadin.ui.*;
import com.vaadin.ui.dnd.DragSourceExtension;
import com.vaadin.ui.dnd.DropTargetExtension;
import com.vaadin.ui.dnd.FileDropHandler;
import com.vaadin.ui.dnd.FileDropTarget;
import com.vaadin.ui.themes.ValoTheme;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class DndField extends VerticalLayout {

    private String homeDir = AppUI.AppUIServlet.getCurrent().getServletContext().getRealPath("/");
    private String dirName = "/tmp"; //можно создавать папку для каждого клиента (id)
    private Random random = new Random();


    public DndField() {

//        Label draggableLabel = new Label("You can grab and drag me");
//        DragSourceExtension<Label> dragSource = new DragSourceExtension<>(draggableLabel);
//        dragSource.setEffectAllowed(EffectAllowed.MOVE);
//        dragSource.setDataTransferText("Hello receiver");
//        dragSource.setDataTransferData("text/html", "<label>Hello receiver</label>");
//        dragSource.addDragStartListener(event -> Notification.show("Drag event started.", Notification.Type.TRAY_NOTIFICATION));
//        dragSource.addDragEndListener(event -> {
//            if (event.isCanceled()){
//                Notification.show("Drag event was canceled.", Notification.Type.TRAY_NOTIFICATION);
//            } else {
//                Notification.show("Drag event finished.", Notification.Type.TRAY_NOTIFICATION);
//            }
//        });
        Label info = new Label("Drag files from the file system");
        VerticalLayout dropTargetLayout = new VerticalLayout(info);
        dropTargetLayout.addStyleName("drop-area");
        dropTargetLayout.setWidth("300px");
        dropTargetLayout.setHeight("300px");

        FileDropTarget<VerticalLayout> fileDropTarget = new FileDropTarget<>(dropTargetLayout, event -> {
            Collection<Html5File> files = event.getFiles();
            File dir = AppUtils.checkDirectoryExists(homeDir, dirName);
//            File dir = new File(homeDir + dirName);
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }

            files.forEach(file -> {

                        File newFile = null;
                        int i = 0;
                        String path = null;
                        do{
                            if (i > 0){
                                int lastIndexOf = file.getFileName().lastIndexOf(".");
                                StringBuffer sb = new StringBuffer(file.getFileName());
                                sb.insert(lastIndexOf, i);
                                path = homeDir + dir.getName() +File.separator /*+ i*/ /*Math.abs(random.nextInt())*/ + sb.toString();
                            }else {
                                path = homeDir + dir.getName() +File.separator + file.getFileName();
                            }
                            newFile = new File(path);
                            i++;
                        }while (newFile.exists());

                        File finalNewFile = newFile;
                        info.setValue(finalNewFile.toString());

                        file.setStreamVariable(new StreamVariable() {
                            @Override
                            public OutputStream getOutputStream() {
                                try {
                                    return new FileOutputStream(finalNewFile);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                    return null;
                                }
                            }

                            @Override
                            public boolean listenProgress() {
                                return true;
                            }

                            @Override
                            public void onProgress(StreamingProgressEvent event) {
                                Notification.show("Progress, byteReceived=" + event.getBytesReceived());
                            }

                            @Override
                            public void streamingStarted(StreamingStartEvent event) {
                                Notification.show("Stream started, filename=" + event.getFileName());
                            }

                            @Override
                            public void streamingFinished(StreamingEndEvent event) {
                                Notification.show("Stream finished, filename=" + event.getFileName());
                            }

                            @Override
                            public void streamingFailed(StreamingErrorEvent event) {
                                Notification.show("Stream failed, filename=" + event.getFileName());
                            }

                            @Override
                            public boolean isInterrupted() {
                                return false;
                            }
                        });
                    }
            );
        });

//        DropTargetExtension<VerticalLayout> dropTarget = new DropTargetExtension<>(dropTargetLayout);
//        dropTarget.setDropEffect(DropEffect.MOVE);
//        dropTarget.addDropListener(event -> {
//            Optional<AbstractComponent> dragComponent = event.getDragSourceComponent();
//            if (dragComponent.isPresent() && dragComponent.get() instanceof Label){
//                dropTargetLayout.addComponent(dragComponent.get());
//                String message = event.getDataTransferData("text/html").get();
//                if (message != null){
//                    Notification.show("Drop event transfer data wid html: " + message);
//                } else {
//                    message = event.getDataTransferText();
//                    Notification.show("Drop event transfer text: " + message);
//                }
//            }
//        });
        addComponents(dropTargetLayout);
        setComponentAlignment(dropTargetLayout, Alignment.MIDDLE_CENTER);

    }

    private File checkIfFileExists(File file){
        String fileName = file.getName();
        String fileNameOrig = fileName;
        String dir = file.getPath();
        int i = 0;
        while (file.exists()) {
            i++;
            int lastIndexOf = fileNameOrig.lastIndexOf(".");
            StringBuilder sb = new StringBuilder(fileNameOrig);
            sb.insert(lastIndexOf, i);
            fileName = sb.toString();
            file = new File(dir + File.separator + fileName);
            fileName = fileNameOrig;
        }
        return file;
    }

}
