package zybandroid.opengl.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import static android.opengl.GLES20.*;
import android.opengl.GLUtils;
import static android.opengl.GLUtils.texImage2D;

import android.util.Log;
import android.util.SparseIntArray;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

/**
 *
 * @author Zybon
 * Created time: 2017.02.12 21:46:23
 */
public class TextureHelper {
    
    private static final String TAG = "TextureHelper";
    private static final SparseIntArray idk = new SparseIntArray();
    private static final HashMap<String, Integer> id_by_name = new HashMap<String, Integer>();

    private static int activateTexture(Context context, String hivo, int resourceId){
        final int[] textureObjectIds = new int[1];
        glGenTextures(1, textureObjectIds, 0);
        
        if (textureObjectIds[0] == 0) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Could not create new OpenGl texture object");
            }
            return 0;
        }
        
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        
        final Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resourceId, options);

        
        if (bitmap == null) {
            glDeleteTextures(1, textureObjectIds, 0);
            if (LoggerConfig.ON) {
                Log.w(TAG, "{"+hivo+"} Resource ID " + resourceId + " could not be decoded.");
            }
            
            return 0;            
        }
        
        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
        
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        
        glGenerateMipmap(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, 0);
        idk.append(resourceId, textureObjectIds[0]);
        return textureObjectIds[0];
    }

    private static int activateTextureFromImageFile(File file){
        final int[] textureObjectIds = new int[1];
        glGenTextures(1, textureObjectIds, 0);
        
        if (textureObjectIds[0] == 0) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Could not create new OpenGl texture object");
            }
            return 0;
        }
        
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        
        final Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        
        if (bitmap == null) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "External image << " + file + " >> could not be decoded.");
            }
            glDeleteTextures(1, textureObjectIds, 0);
            return 0;            
        }
        
        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST_MIPMAP_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST_MIPMAP_LINEAR); 
        
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
        bitmap.recycle();
        
        glGenerateMipmap(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, 0);
        id_by_name.put(file.getAbsolutePath(), textureObjectIds[0]);
        return textureObjectIds[0];
    }
    
    private static int activateTextureFromAssetImageFile(AssetManager assetManager, String name){
        final int[] textureObjectIds = new int[1];
        glGenTextures(1, textureObjectIds, 0);
        
        if (textureObjectIds[0] == 0) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Could not create new OpenGl texture object");
            }
            return 0;
        }
        
        try {
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inScaled = false;
            InputStream ims = assetManager.open(name);
            final Bitmap bitmap = BitmapFactory.decodeStream(ims);// options);

            if (bitmap == null) {
                if (LoggerConfig.ON) {
                    Log.w(TAG, "Asset image << " + name + " >> could not be decoded.");
                }
                glDeleteTextures(1, textureObjectIds, 0);
                return 0;            
            }

            glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR_MIPMAP_LINEAR); 

            GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0);
            bitmap.recycle();

            glGenerateMipmap(GL_TEXTURE_2D);
            glBindTexture(GL_TEXTURE_2D, 0);
            id_by_name.put(name, textureObjectIds[0]);
            return textureObjectIds[0];
        }
        catch (IOException ioex){
            Log.i(TAG, ioex.getLocalizedMessage());
            return 0;
        }
    }
        
//    public static int getGlTextureIdFromName(Context context, String name){
//        if (MainActivity.ASSET_FROM_OUTSIDE) {
//            File SD_CARD = Environment.getExternalStorageDirectory();
//            File file = new File(SD_CARD, "programs_assets/zybonotopia/drawable-nodpi/"+name);
//            return getGlTextureIdFromImageFile(file);
//        }   
//        int resId = DataReadHelper.getResourceIdFromString(context, name);
//        return getGlTextureIdFromResID(context, name, resId);
//    }
    
    public static int getGlTextureIdFromResID(Context context, String hivo, int resourceId) {
        int id = idk.get(resourceId, -1);
        if (id == -1) {
            return activateTexture(context, hivo, resourceId);
        }
        return id;
    }
    
    public static int getGlTextureIdFromImageFile(File file) {
        if (id_by_name.containsKey(file.getAbsolutePath())) {
            return id_by_name.get(file.getAbsolutePath());
        }
        else {
            return activateTextureFromImageFile(file);
        }
        
    }  
    
    public static int getGlTextureIdFromAssetFile(AssetManager assetManager, String name) {
        if (id_by_name.containsKey(name)) {
            return id_by_name.get(name);
        }
        else {
            return activateTextureFromAssetImageFile(assetManager, name);
        }
        
    }       
    
    public static void reset(){
        idk.clear();
        id_by_name.clear();
    }
    
    public static int activateTextureFromBitmap(Bitmap bm){
        final int[] textureObjectIds = new int[1];
        glGenTextures(1, textureObjectIds, 0);
        
        if (textureObjectIds[0] == 0) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Could not create new OpenGl texture object");
            }
            return 0;
        }
        
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        
        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0]);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);  
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bm, 0);
        bm.recycle();
        
//        glGenerateMipmap(GL_TEXTURE_2D);
        glBindTexture(GL_TEXTURE_2D, 0);
//        idk.append(resourceId, textureObjectIds[0]);
        return textureObjectIds[0];
    }
    
/**
     * Loads a cubemap texture from the provided resources and returns the
     * texture ID. Returns 0 if the load failed.
     * 
     * @param context
     * @param cubeResources
     *            An array of resources corresponding to the cube map. Should be
     *            provided in this order: left, right, bottom, top, front, back.
     * @return
     */
    public static int loadCubeMap(Context context, int[] cubeResources) {       
        final int[] textureObjectIds = new int[1];
        glGenTextures(1, textureObjectIds, 0);

        if (textureObjectIds[0] == 0) {
            if (LoggerConfig.ON) {
                Log.w(TAG, "Could not generate a new OpenGL texture object.");
            }
            return 0;
        }      
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        final Bitmap[] cubeBitmaps = new Bitmap[6];
        for (int i = 0; i < 6; i++) {
            cubeBitmaps[i] =
                BitmapFactory.decodeResource(context.getResources(),
                    cubeResources[i], options);

            if (cubeBitmaps[i] == null) {
                if (LoggerConfig.ON) {
                    Log.w(TAG, "Resource ID " + cubeResources[i]
                        + " could not be decoded.");
                }
                glDeleteTextures(1, textureObjectIds, 0);
                return 0;
            }
        }
        // Linear filtering for minification and magnification
        glBindTexture(GL_TEXTURE_CUBE_MAP, textureObjectIds[0]);
        
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR);  
        texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, cubeBitmaps[0], 0);
        texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, cubeBitmaps[1], 0);
        
        texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, cubeBitmaps[2], 0);
        texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, cubeBitmaps[3], 0);
        
        texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, cubeBitmaps[4], 0);
        texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, cubeBitmaps[5], 0);                
        glBindTexture(GL_TEXTURE_2D, 0);
        
        for (Bitmap bitmap : cubeBitmaps) {
            bitmap.recycle();
        }        

        return textureObjectIds[0];        
    }    
    
    
}
