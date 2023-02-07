package zybandroid.opengl.util;

import android.content.Context;
import android.content.res.Resources;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 * @author zybon
 * Created 2018.01.17. 15:11:31
 */
public class RawFileReader {
    
    public static byte[] readRawFileFromResource(Context context, int resourceId){
        ByteArrayOutputStream bab = new ByteArrayOutputStream(5000);
        try {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            byte[] buffer = new byte[1024];
            int b;
            while (true) {
                b = inputStream.read(buffer);
                if (b == -1) {break;}
                bab.write(buffer, 0, b);
            }
            inputStream.close();
        }
        catch (IOException e) {
            throw new RuntimeException(
            "Could not open resource: " + resourceId, e);
        } catch (Resources.NotFoundException nfe) {
        throw new RuntimeException("Resource not found: " + resourceId, nfe);
        }
        return bab.toByteArray();
    }
    
    public static byte[] readRawFileFromAsset(Context context, String name){
//        if (MainActivity.ASSET_FROM_OUTSIDE) {
//            File SD_CARD = Environment.getExternalStorageDirectory();
//            File file = new File(SD_CARD, "programs_assets/zybonotopia/assets/"+name);
//            return readRawFileFromFile(file);
//        }        
        
        ByteArrayOutputStream bab = new ByteArrayOutputStream(5000);
        try {
            InputStream inputStream = context.getAssets().open(name);
            byte[] buffer = new byte[1024];
            int b;
            while (true) {
                b = inputStream.read(buffer);
                if (b == -1) {break;}
                bab.write(buffer, 0, b);
            }
            inputStream.close();
        }
        catch (IOException e) {
            throw new RuntimeException(
            "Could not open asset: " + name, e);
        } 
        return bab.toByteArray();
    }    
    
    public static byte[] readRawFileFromFile(File file){
        ByteArrayOutputStream bab = new ByteArrayOutputStream(5000);
        try {
            InputStream inputStream = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int b;
            while (true) {
                b = inputStream.read(buffer);
                if (b == -1) {break;}
                bab.write(buffer, 0, b);
            }
            inputStream.close();
        }
        catch (IOException e) {
            throw new RuntimeException(
            "Could not open file: " + file, e);
        } 
        return bab.toByteArray();
    }
        

}
