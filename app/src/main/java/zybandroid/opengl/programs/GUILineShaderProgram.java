package zybandroid.opengl.programs;

import android.opengl.GLES20;
import static android.opengl.GLES20.glUniform1f;
import zybandroid.opengl.geometry.Vector3D;
import zybandroid.opengl.util.ZybColor;

/**
 *
 * @author zybon
 * Created 2019.03.27. 15:04:19
 */
public class GUILineShaderProgram extends ShaderProgram{
    
    private static GUILineShaderProgram guiLineShaderProgram;
    
    public static void setGUILineShaderProgram(GUILineShaderProgram guiLineShaderProgram) {
        GUILineShaderProgram.guiLineShaderProgram = guiLineShaderProgram;
    }    

    public static GUILineShaderProgram getGUILineShaderProgram() {
        return guiLineShaderProgram;
    }     
    
    
    private final int uPositionsLocation;
    private final int uAlphaLocation;
    private final int uColorLocation;
    
    private final int aPositionLocation;

    public GUILineShaderProgram(String vertexShader, String fragmentShader, String name) {
        super(vertexShader, fragmentShader, name);
        uPositionsLocation = getUniformLocation("u_Positions");
        uAlphaLocation = getUniformLocation("u_alpha");
        uColorLocation = getUniformLocation("u_Color");
        
        aPositionLocation = getAttribLocation("a_Position");
        
    }
    
    public void loadAlphaUniform(float alpha){
        glUniform1f(uAlphaLocation, alpha);
    }  
    
    public void loadColorUniform(ZybColor color){
        GLES20.glUniform4fv(uColorLocation, 1, color.rgba, 0);
    }    
    
    float[] load = new float[2*4];
    public void loadPositionsUniform(Vector3D start, Vector3D middle, Vector3D end){
        System.arraycopy(start.xyzw, 0, load, 0, 4);
//        System.arraycopy(middle.xyzw, 0, load, 4, 4);
//        System.arraycopy(middle.xyzw, 0, load, 8, 4);
        System.arraycopy(end.xyzw, 0, load, 4, 4);
        GLES20.glUniform4fv(uPositionsLocation, 2, load, 0);
    }       

    public int getaPositionLocation() {
        return aPositionLocation;
    }
    
    

}

