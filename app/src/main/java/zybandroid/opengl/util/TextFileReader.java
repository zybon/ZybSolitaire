package zybandroid.opengl.util;

import android.content.Context;
import android.content.res.Resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author Zybon
 * Created time: 2017.02.11 19:01:55
 */
public class TextFileReader {
    
    public static String readTextFromFile(File textFile){
        StringBuilder str = new StringBuilder();
        
        try {
            InputStream inputStream = new FileInputStream(textFile);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            
            String nextLine;
            
            while ((nextLine = bufferedReader.readLine()) != null){
                str.append(nextLine);
                str.append('\n');
            }
        }
        catch (IOException e) {
            throw new RuntimeException(
            "Could not open text file: " + textFile, e);
        } 
        return str.toString();
    }    
    
    public static String readTextFromResource(Context context, int resourceId){
        StringBuilder str = new StringBuilder();
        
        try {
            InputStream inputStream = context.getResources().openRawResource(resourceId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            
            String nextLine;
            
            while ((nextLine = bufferedReader.readLine()) != null){
                str.append(nextLine);
                str.append('\n');
            }
        }
        catch (IOException e) {
            throw new RuntimeException(
            "Could not open resource: " + resourceId, e);
        } catch (Resources.NotFoundException nfe) {
        throw new RuntimeException("Resource not found: " + resourceId, nfe);
        }
        return str.toString();
    }
    
    public static String readTextFromAssets(Context context, String name){
//        if (MainActivity.ASSET_FROM_OUTSIDE) {
//            File SD_CARD = Environment.getExternalStorageDirectory();
//            File file = new File(SD_CARD, "programs_assets/zybonotopia/assets/"+name);
//            return readTextFromFile(file);
//        }
        
        StringBuilder str = new StringBuilder();
        
        try {
            InputStream inputStream = context.getAssets().open(name);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            
            String nextLine;
            
            while ((nextLine = bufferedReader.readLine()) != null){
                str.append(nextLine);
                str.append('\n');
            }
        }
        catch (IOException e) {
            throw new RuntimeException(
            "Could not open asset: " + name, e);
        } 
        return str.toString();
    }    

}
