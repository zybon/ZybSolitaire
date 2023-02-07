package zybandroid.opengl.util;

import zybandroid.opengl.geometry.Vector3D;
import static zybandroid.opengl.util.ZybMath.clamp;

/**
 *
 * @author zybon
 * Created 2019.03.26. 21:21:17
 */
public class ZybColor {
    
    public static final ZybColor RED = new ZybColor(0xffff0000);
    public static final ZybColor GREEN = new ZybColor(0xff00ff00);
    public static final ZybColor BLUE = new ZybColor(0xff0000ff);
    public static final ZybColor WHITE = new ZybColor(0xffffffff);
    public static final ZybColor CYAN = new ZybColor(0xff00ffff);
    public static final ZybColor BLACK = new ZybColor(0xcc000000);
    public static final ZybColor YELLOW = new ZybColor(0xffffff00);
    public static final ZybColor LIGHTGREY = new ZybColor(0xffdddddd);
    public static final ZybColor DARKGREY = new ZybColor(0xff222222);
    public static final ZybColor MAGENTA = new ZybColor(0xffff00ff);
    public static final ZybColor ORANGE = new ZybColor(0xffff8500);
    
    public static final ZybColor EMPTY = new ZybColor(0);
    
    public float[] rgba = new float[4];

    public ZybColor() {
    }
    
    public ZybColor(String htmlColor) {
        set(htmlColor);
    }     
    
    public ZybColor(ZybColor zybColor) {
        this.set(zybColor.rgba);
    }    
    
    public ZybColor(float[] rgba) {
        set(rgba);
    }      
    
    public ZybColor(float r, float g, float b, float a) {
        set(r, g, b, a);
    }
    
    public ZybColor(int argb) {
        set(argbIntToFloatArray(argb));
    }     
    
    public final void set(ZybColor zybColor) {
        set(zybColor.rgba);
    }    

    public final void set(float[] rgba) {
        setR(rgba[0]);
        setG(rgba[1]);
        setB(rgba[2]);
        if (rgba.length == 3) {
            setA(1.0f);
        }
        else {
            setA(rgba[3]);
        }
    }
    
    public final void set(String htmlColor){
        if (!htmlColor.startsWith("#")) {
            throw new AssertionError("This is not valid html color code: \""+htmlColor+"\"");
        }
        if (!(htmlColor.length() == 7 || htmlColor.length() == 9)) {
            throw new AssertionError("This is not valid html color code: \""+htmlColor+"\"\n"
                    + "length is = "+htmlColor.length());
        }        
        int index = 1;
        int a = 255;
        if (htmlColor.length() == 9){
            a = Integer.parseInt(htmlColor.substring(index, index+2), 16);
            index += 2;
        }
        int r = Integer.parseInt(htmlColor.substring(index, index+2), 16);
        index += 2;
        int g = Integer.parseInt(htmlColor.substring(index, index+2), 16);
        index += 2;
        int b = Integer.parseInt(htmlColor.substring(index, index+2), 16);
        index += 2;
        set((float)r/255f, (float)g/255f, (float)b/255f, (float)a/255f);    
    }    

    public final void set(float r, float g, float b, float a) {
        setR(r);
        setG(g);
        setB(b);
        setA(a);        
    }
    
    public final void set(int argb) {
        this.rgba[0] = ((float)((argb>>16)&0xff))/255f;
        this.rgba[1] = ((float)((argb>>8)&0xff))/255f;
        this.rgba[2] = ((float)((argb)&0xff))/255f;
        this.rgba[3] = ((float)((argb>>24)&0xff))/255f;
    }    
    
    public static final float[] argbIntToFloatArray(int argb){
        return new float[]{
            ((float)((argb>>16)&0xff))/255f,
            ((float)((argb>>8)&0xff))/255f,
            ((float)((argb)&0xff))/255f,
            ((float)((argb>>24)&0xff))/255f
        };
    }
    
    
    
    public final void setR(float r) {
        this.rgba[0] = clamp(r, 0.0f, 1.0f);
    }    
    
    public final void setG(float g) {
        this.rgba[1] = clamp(g, 0.0f, 1.0f);
    } 
    
    public final void setB(float b) {
        this.rgba[2] = clamp(b, 0.0f, 1.0f);
    } 
    
    public final void setA(float a) {
        this.rgba[3] = clamp(a, 0.0f, 1.0f);
    }     
    
    public final float getR() {
        return this.rgba[0];
    }    
    
    public final float getG() {
        return this.rgba[1];
    } 
    
    public final float getB() {
        return this.rgba[2];
    } 
    
    public final float getA() {
        return this.rgba[3];
    }      
    
    public static ZybColor mix(ZybColor color1, ZybColor color2, float blendFactor) {
        ZybColor mix = new ZybColor();
        mix.rgba[0] = ZybMath.clamp(ZybMath.mix(color1.rgba[0], color2.rgba[0], blendFactor), 0.0f, 1.0f);
        mix.rgba[1] = ZybMath.clamp(ZybMath.mix(color1.rgba[1], color2.rgba[1], blendFactor), 0.0f, 1.0f);
        mix.rgba[2] = ZybMath.clamp(ZybMath.mix(color1.rgba[2], color2.rgba[2], blendFactor), 0.0f, 1.0f);
        mix.rgba[3] = ZybMath.clamp(ZybMath.mix(color1.rgba[3], color2.rgba[3], blendFactor), 0.0f, 1.0f);
        return mix;
    }   
    
    public void blend(ZybColor color1, ZybColor color2, float blendFactor) {
        rgba[0] = ZybMath.clamp(ZybMath.mix(color1.rgba[0], color2.rgba[0], blendFactor), 0.0f, 1.0f);
        rgba[1] = ZybMath.clamp(ZybMath.mix(color1.rgba[1], color2.rgba[1], blendFactor), 0.0f, 1.0f);
        rgba[2] = ZybMath.clamp(ZybMath.mix(color1.rgba[2], color2.rgba[2], blendFactor), 0.0f, 1.0f);
        rgba[3] = ZybMath.clamp(ZybMath.mix(color1.rgba[3], color2.rgba[3], blendFactor), 0.0f, 1.0f);        
    }

    public void blendWith(ZybColor color2, float blendFactor) {
        rgba[0] = ZybMath.clamp(ZybMath.mix(rgba[0], color2.rgba[0], blendFactor), 0.0f, 1.0f);
        rgba[1] = ZybMath.clamp(ZybMath.mix(rgba[1], color2.rgba[1], blendFactor), 0.0f, 1.0f);
        rgba[2] = ZybMath.clamp(ZybMath.mix(rgba[2], color2.rgba[2], blendFactor), 0.0f, 1.0f);
        rgba[3] = ZybMath.clamp(ZybMath.mix(rgba[3], color2.rgba[3], blendFactor), 0.0f, 1.0f);
    }

    public void blendWith_ARGBint(int argb, float blendFactor) {
        rgba[0] = ZybMath.clamp(ZybMath.mix(rgba[0], ((float)((argb>>16)&0xff))/255f, blendFactor), 0.0f, 1.0f);
        rgba[1] = ZybMath.clamp(ZybMath.mix(rgba[1], ((float)((argb>>8)&0xff))/255f, blendFactor), 0.0f, 1.0f);
        rgba[2] = ZybMath.clamp(ZybMath.mix(rgba[2], ((float)((argb)&0xff))/255f, blendFactor), 0.0f, 1.0f);
        rgba[3] = ZybMath.clamp(ZybMath.mix(rgba[3], ((float)((argb>>24)&0xff))/255f, blendFactor), 0.0f, 1.0f);
    }

    public void scaleRGB(float scale) {
        this.rgba[0] = clamp(this.rgba[0]*scale, 0.0f, 1.0f);
        this.rgba[1] = clamp(this.rgba[1]*scale, 0.0f, 1.0f);
        this.rgba[2] = clamp(this.rgba[2]*scale, 0.0f, 1.0f);
    }    

    @Override
    public String toString() {
        return "ZybColor["+
                " r:"+Vector3D.kerekit(rgba[0], 2)+","+
                " g:"+Vector3D.kerekit(rgba[1], 2)+","+
                " b:"+Vector3D.kerekit(rgba[2], 2)+","+
                " a:"+Vector3D.kerekit(rgba[3], 2)+","+
                "]";
    }




    
    
    
}
