package zybandroid.opengl.util;

/**
 *
 * @author zybon
 * Created time: 2019.05.23 15:42:38
 */
public class ZybMath {
    
    public static int clamp(int value, int minValue, int maxValue){
        return Math.min(maxValue, Math.max(value, minValue));
    }
    
    public static float clamp(float value, float minValue, float maxValue){
        return Math.min(maxValue, Math.max(value, minValue));
    }    
    
    public static float mix(float v1, float v2, float blendFactor){
        return v1+(v2-v1)*blendFactor;
    }    


}
