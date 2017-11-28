package com.sergeeva.demoPdf;

import java.io.File;

public class AppUtils {
    static int i = 0;

    public static File checkIfFileExists(File file){
        String fileName = file.getName();
        String fileNameOrig = fileName;
        String dir = file.getPath();

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
/*
проверяет, существует ли директория, если существует возвращает существующую, если нет, создает новую
 */
    public static File checkDirectoryExists(String homeDir, String dirName){
        File dir = new File(homeDir + dirName);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return dir;
    }
}
