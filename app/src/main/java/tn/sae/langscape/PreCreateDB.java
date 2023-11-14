package tn.sae.langscape;
import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class PreCreateDB {
    static String destPath;
    static String destPathwithFilename;
    // Lets define copyDB() method
    public static void copyDB(Context context){
        // Defile two String variables containing path upto "database" folder and "CTDB" file respectively
        destPath = "/data/data/" + context.getPackageName() + "/databases";
        destPathwithFilename = destPath+"/CTDB";
        File fPath = new File(destPath);
        File fPathWithName = new File(destPathwithFilename);

        if(!fPath.exists()){
            fPath.mkdirs();

            try {
                rawCopy(context.getAssets().open("CTDB"), new FileOutputStream(destPathwithFilename));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static void rawCopy(InputStream inputStream, OutputStream outputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int length;
       while((length = inputStream.read(buffer)) > 0){
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.close();
    }

    public static void resetDB(Context context) {
        try {
            rawCopy(context.getAssets().open("CTDB"), new FileOutputStream(destPathwithFilename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}