package AsciiImage.Util;

import java.io.File;

public final class FileUtil {

    public static String getFileExtension(File file) {
        String name = file.getName();
        int lastIndex = name.lastIndexOf(".");
        if (lastIndex == -1) {
            return "";
        }
        return name.substring(lastIndex+1).toLowerCase();
    }

    public static String removeFileExt(File file) {
        String fileName = file.getName();
        String name = "";
        String[] arr = fileName.split("\\.");
        for (int i = 0; i < arr.length; i++) {
            if (i != arr.length-1) {
                name += arr[i];
            }
        }
        return name;
    }

}
