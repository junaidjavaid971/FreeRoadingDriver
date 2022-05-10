package com.apps.freeroadingdriver.utils;

import android.content.Context;

import java.io.File;

public class FileCache {

    private static final String FOLDER_NAME=".Wedoshoes";
    private final File rootDir;
    private static FileCache fileCache;
    public static FileCache getInstance(Context context){
        if(fileCache==null) {
            fileCache = new FileCache(context);
        }
        return fileCache;

    }

    private FileCache(Context context) {

        // Find the dir at SDCARD to save cached images
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {

            rootDir = new File(
                    android.os.Environment.getExternalStorageDirectory(),
                    FOLDER_NAME);
        } else {
            // if checking on simulator the create cache dir in your application
            // context
            rootDir = context.getCacheDir();
        }

        if (!rootDir.exists()) {
            // create cache dir in your application context
            rootDir.mkdirs();
        }
    }

    public File getFile(String url, boolean isHres){
        //Identify images by hashcode or encode by URLEncoder.encode.
        String filename=getFileName(url);
        if(!isHres){
            filename = "lres_"+ System.currentTimeMillis() + filename;
        }
        return new File(rootDir, filename);
    }

    public String getFileName(String url){
        String name = url.substring(url.lastIndexOf('/')+1);
        int index=name.indexOf('?');
        if (index>=0) {
            name = name.substring(0, index);
        }
        return name;
    }



    public File createFile(String filename){
        return new File(rootDir, filename);
    }

    public void clear(){
        // list all files inside cache directory
        File[] files= rootDir.listFiles();
        if(files==null) {
            return;
        }
        //delete all cache directory files
        for(File f:files) {
            f.delete();
        }
    }

    /**
     * Delete file from cache
     * @param filename
     * @return whether file deleted or not
     */
    public boolean delete(String filename) {
        return new File(rootDir, getFileName(filename)).delete();
    }
}