/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package zybandroid.opengl.blenderdatas;

import android.content.Context;
import android.util.Log;
import static zybandroid.opengl.blenderdatas.DataReadHelper.BYTES_PER_INT;
import zybandroid.opengl.geometry.Vector3D;
import zybandroid.opengl.util.TextureHelper;
import zybandroid.opengl.util.ZybColor;

/**
 *
 * @author root
 */
public class Material {
    
    private static final String TAG = "Material";
    
    private byte[] bytes;
    private int byteIndex;    
    
    private String name;
    private ZybColor diffuseColor;
    private float diffuseIntensity;
    private boolean hasTexture;
    private String textureName;
    private int textureResId;
    private int textureGlId;
    private Vector3D textureScale;
    private boolean mixTextureWithDiffuse;

    public Material() {
    }
    
    public int readData(Context context, byte[] bytes, int startIndex){
        this.bytes = bytes;
        this.byteIndex = startIndex;
        readName();
        readColor();
        readTexture(context);
        return byteIndex;
    }
   
    private void readName(){
        int nameLength = (bytes[byteIndex]&0xff);
        byteIndex += 1;
        name = DataReadHelper.readString(byteIndex, bytes, nameLength);
        byteIndex += nameLength;     
//        Log.i(TAG, "material name: "+name);
        
    }    
    
    private void readColor(){
        diffuseColor = new ZybColor(DataReadHelper.readFloatArray(byteIndex, bytes, 3));
        byteIndex += 3*BYTES_PER_INT;   
//        Log.i(TAG, "diffuseColor: "+diffuseColor);
        
        diffuseIntensity = DataReadHelper.readFloat(byteIndex, bytes);
        byteIndex += BYTES_PER_INT; 
//        Log.i(TAG, "diffuseIntensity: "+diffuseIntensity);
    } 
    
    private void readTexture(Context context){
        hasTexture = (bytes[byteIndex] == 1);
        byteIndex += 1;
//        Log.i(TAG, "hasUvTexture: "+hasUvTexture);
        if (hasTexture) {
            int nameLength = (bytes[byteIndex]&0xff);
            byteIndex += 1;
            textureName = DataReadHelper.readString(byteIndex, bytes, nameLength);
//            Log.i(TAG, "textureName: "+textureName);
            textureResId = DataReadHelper.getResourceIdFromString(context, textureName);
            if (textureResId == 0) {
                throw new AssertionError("'"+textureName+"' texture in '"+name+"' material "
                        + "not founded in resources");
            }
            textureGlId = TextureHelper.getGlTextureIdFromResID(context, name, textureResId);
//            Log.i(TAG, "textureResId: "+textureResId);
//            Log.i(TAG, "\n");
            byteIndex += nameLength; 
            
            textureScale = new Vector3D(DataReadHelper.readFloatArray(byteIndex, bytes, 3));
            byteIndex += 3*BYTES_PER_INT;  
            
            mixTextureWithDiffuse = (bytes[byteIndex] == 1);
            byteIndex += 1;     
            
            if (!mixTextureWithDiffuse){
                diffuseColor.set(0);
            }
        }
    }  
    
    public String getName() {
        return name;
    }

    public ZybColor getDiffuseColor() {
        return diffuseColor;
    }

    public float getDiffuseIntensity() {
        return diffuseIntensity;
    }

    public boolean isHasTexture() {
        return hasTexture;
    }

    public String getTextureName() {
        return textureName;
    }

    public int getTextureResId() {
        return textureResId;
    }

    public int getTextureGlId() {
        return textureGlId;
    }

    public Vector3D getTextureScale() {
        return textureScale;
    }

    public boolean isMixTextureWithDiffuse() {
        return mixTextureWithDiffuse;
    }
    
    

    
}
