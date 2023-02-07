package zybandroid.opengl.blenderdatas;

import android.content.Context;
import android.util.Log;


/**
 *
 * @author zybon
 * Created 2018.08.12. 10:30:20
 */
public class DataReadHelper {
    
    public static final int BYTES_PER_INT = 4;
    public static final float INT_TO_FLOAT_DIVIDER = 100000.0f;
    
    public static final int readInt(int startIndex, byte[] bytes){
        int value = 0;
        for (int i = 0; i < BYTES_PER_INT; i++) {
            value = value<<8 | ((bytes[startIndex+i])&0xff);
        }
        return value;
    }
    
    public static final float readFloat(int startIndex, byte[] bytes){
        return getFloatFromInt(readInt(startIndex, bytes));
    }    

    public static final float[] readFloatArray(int startIndex, byte[] bytes, int arrayLength){
        float[] array = new float[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            array[i] = readFloat(startIndex, bytes);
            startIndex += BYTES_PER_INT;
        }
        return array;
    }
    
    public static final float getFloatFromInt(float value){
        return value/INT_TO_FLOAT_DIVIDER;
    }   
    
    public static final short[] readShortArray(int startIndex, byte[] bytes, int arrayLength){
        short[] array = new short[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            array[i] = (short)readInt(startIndex, bytes);
            startIndex += BYTES_PER_INT;
        }
        return array;
    }     

    public static final int[] readIntArray(int startIndex, byte[] bytes, int arrayLength){
        int[] array = new int[arrayLength];
        for (int i = 0; i < arrayLength; i++) {
            array[i] = readInt(startIndex, bytes);
            startIndex += BYTES_PER_INT;
        }
        return array;
    }    

    public static final String readString(int startIndex, byte[] bytes, int dataLength){
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < dataLength; i++) {
            sb.append((char)bytes[startIndex+i]);
        }
        return sb.toString();
    }         
    
    public static int getResourceIdFromString(Context context, String res_name){
//        Log.i("getResourceIdFromString", "res_name: #"+res_name+"#");
        return context.getResources().getIdentifier(res_name, "drawable", context.getPackageName());
    }    

}
